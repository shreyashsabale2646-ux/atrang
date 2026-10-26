package in.ac.iiitt.atrang.service;

import in.ac.iiitt.atrang.dto.*;
import in.ac.iiitt.atrang.entity.Event;
import in.ac.iiitt.atrang.entity.EventCategory;
import in.ac.iiitt.atrang.entity.EventSession;
import in.ac.iiitt.atrang.exception.ConflictException;
import in.ac.iiitt.atrang.exception.ResourceNotFoundException;
import in.ac.iiitt.atrang.repository.EventCategoryRepository;
import in.ac.iiitt.atrang.repository.EventRepository;
import in.ac.iiitt.atrang.repository.EventSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventCategoryRepository categoryRepository;
    private final AuditService auditService;

    public EventService(EventRepository eventRepository,
                        EventCategoryRepository categoryRepository,
                        AuditService auditService) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<EventDetailDto> getPublicEvents(String categoryCode, String search) {
        Specification<Event> spec = EventSpecifications.withPublicFilters(categoryCode, search);
        Sort sort = Sort.by(Sort.Direction.ASC, "displayOrder").and(Sort.by(Sort.Direction.ASC, "id"));
        return eventRepository.findAll(spec, sort).stream()
                .map(this::mapToDetailDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventDetailDto getPublicEventBySlug(String slug) {
        Event event = eventRepository.findBySlugAndIsPublishedTrue(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with slug: " + slug));
        return mapToDetailDto(event);
    }

    @Transactional(readOnly = true)
    public Page<EventSummaryDto> getAdminEvents(Long categoryId, Boolean isPublished, String search, Pageable pageable) {
        Specification<Event> spec = EventSpecifications.withAdminFilters(categoryId, isPublished, search);
        return eventRepository.findAll(spec, pageable).map(this::mapToSummaryDto);
    }

    @Transactional(readOnly = true)
    public EventDetailDto getAdminEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return mapToDetailDto(event);
    }

    @Transactional
    public EventDetailDto createEvent(EventCreateRequest req, String performer) {
        if (eventRepository.existsBySlug(req.getSlug())) {
            throw new ConflictException("Slug already in use: " + req.getSlug());
        }

        EventCategory category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + req.getCategoryId()));

        Event event = new Event();
        applyEventFields(event, category, req);

        Event saved = eventRepository.save(event);

        auditService.log(saved.getId(), null, "CREATE", "Created event: " + saved.getTitle(), performer, null);
        return mapToDetailDto(saved);
    }

    @Transactional
    public EventDetailDto updateEvent(Long id, EventUpdateRequest req, String performer) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        if (eventRepository.existsBySlugAndIdNot(req.getSlug(), id)) {
            throw new ConflictException("Slug already in use: " + req.getSlug());
        }

        EventCategory category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + req.getCategoryId()));

        boolean oldPublishStatus = Boolean.TRUE.equals(event.getIsPublished());
        applyEventFields(event, category, req);

        Event updated = eventRepository.save(event);

        String auditAction = "UPDATE";
        if (!oldPublishStatus && Boolean.TRUE.equals(updated.getIsPublished())) {
            auditAction = "PUBLISH";
        } else if (oldPublishStatus && Boolean.FALSE.equals(updated.getIsPublished())) {
            auditAction = "UNPUBLISH";
        }
        auditService.log(updated.getId(), null, auditAction, "Updated event: " + updated.getTitle(), performer, null);

        return mapToDetailDto(updated);
    }

    @Transactional
    public EventDetailDto togglePublication(Long id, String performer) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        boolean newStatus = !Boolean.TRUE.equals(event.getIsPublished());
        event.setIsPublished(newStatus);
        Event saved = eventRepository.save(event);

        String action = newStatus ? "PUBLISH" : "UNPUBLISH";
        auditService.log(saved.getId(), null, action, (newStatus ? "Published" : "Unpublished") + " event: " + saved.getTitle(), performer, null);

        return mapToDetailDto(saved);
    }

    @Transactional
    public void deleteEvent(Long id, String performer) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        String title = event.getTitle();
        eventRepository.delete(event);
        auditService.log(null, null, "DELETE", "Deleted event: " + title + " (ID: " + id + ")", performer, null);
    }

    private void applyEventFields(Event event, EventCategory category, EventPayload req) {
        event.setCategory(category);
        event.setSlug(req.getSlug().trim().toLowerCase());
        event.setTitle(req.getTitle().trim());
        event.setSummary(req.getSummary().trim());
        event.setDescription(req.getDescription());
        event.setOrganizer(req.getOrganizer() != null ? req.getOrganizer().trim() : "");
        if (req.getParticipation() != null) event.setParticipation(req.getParticipation());
        event.setTeamSize(blankToNull(req.getTeamSize()));
        event.setDuration(blankToNull(req.getDuration()));
        event.setPrize(blankToNull(req.getPrize()));
        event.setVenue(req.getVenue().trim());
        event.setEligibility(blankToNull(req.getEligibility()));
        event.setFee(blankToNull(req.getFee()));
        event.setRegistrationUrl(blankToNull(req.getRegistrationUrl()));
        event.setRegistrationDeadline(req.getRegistrationDeadline());
        if (req.getIsPublished() != null) event.setIsPublished(req.getIsPublished());
        if (req.getDisplayOrder() != null) event.setDisplayOrder(req.getDisplayOrder());
        event.setHighlightsList(cleanLines(req.getHighlights()));
        event.setRulesList(cleanLines(req.getRules()));
        event.setCoordinatorsList(req.getCoordinators() == null ? List.of() : req.getCoordinators().stream()
                .filter(c -> c != null && c.getName() != null && !c.getName().isBlank())
                .map(c -> new CoordinatorDto(c.getName().trim(), blankToNull(c.getPhone())))
                .collect(Collectors.toList()));
        replaceSessions(event, req.getSessions());
    }

    private void replaceSessions(Event event, List<EventSessionRequest> requests) {
        event.getSessions().clear();
        if (requests == null) return;
        for (EventSessionRequest sReq : requests) {
            event.getSessions().add(new EventSession(
                    event,
                    sReq.getFestDay(),
                    sReq.getStartTime(),
                    sReq.getEndTime(),
                    sReq.getSessionTitle(),
                    sReq.getVenue(),
                    sReq.getDetails()
            ));
        }
    }

    private static List<String> cleanLines(List<String> lines) {
        if (lines == null) return List.of();
        return lines.stream()
                .filter(s -> s != null && !s.isBlank())
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private EventSummaryDto mapToSummaryDto(Event e) {
        EventSummaryDto dto = new EventSummaryDto();
        dto.setId(e.getId());
        dto.setSlug(e.getSlug());
        dto.setTitle(e.getTitle());
        dto.setSummary(e.getSummary());
        if (e.getCategory() != null) {
            dto.setCategoryId(e.getCategory().getId());
            dto.setCategoryCode(e.getCategory().getCode());
            dto.setCategoryName(e.getCategory().getName());
            dto.setCategoryColor(e.getCategory().getColorHex());
            dto.setCategoryIcon(e.getCategory().getIconName());
        }
        dto.setParticipation(e.getParticipation());
        dto.setTeamSize(e.getTeamSize());
        dto.setDuration(e.getDuration());
        dto.setPrize(e.getPrize());
        dto.setVenue(e.getVenue());
        dto.setFee(e.getFee());
        dto.setRegistrationUrl(e.getRegistrationUrl());
        dto.setIsPublished(e.getIsPublished());
        dto.setDisplayOrder(e.getDisplayOrder());
        dto.setSessionCount(e.getSessions() != null ? e.getSessions().size() : 0);
        dto.setCreatedAt(e.getCreatedAt());

        dto.setHighlights(e.getHighlightsList());
        dto.setRules(e.getRulesList());
        dto.setCoordinators(e.getCoordinatorsList());

        if (e.getSessions() != null) {
            List<ScheduleSlotDto> slots = e.getSessions().stream()
                    .map(s -> new ScheduleSlotDto(s.getFestDay(), s.getTimeWindow(), s.getSessionTitle(), s.getVenue()))
                    .collect(Collectors.toList());
            dto.setSchedule(slots);
        }

        return dto;
    }

    private EventDetailDto mapToDetailDto(Event e) {
        EventDetailDto dto = new EventDetailDto();
        dto.setId(e.getId());
        dto.setSlug(e.getSlug());
        dto.setTitle(e.getTitle());
        dto.setSummary(e.getSummary());
        dto.setDescription(e.getDescription());
        if (e.getCategory() != null) {
            dto.setCategoryId(e.getCategory().getId());
            dto.setCategoryCode(e.getCategory().getCode());
            dto.setCategoryName(e.getCategory().getName());
            dto.setCategoryColor(e.getCategory().getColorHex());
            dto.setCategoryIcon(e.getCategory().getIconName());
        }
        dto.setOrganizer(e.getOrganizer());
        dto.setParticipation(e.getParticipation());
        dto.setTeamSize(e.getTeamSize());
        dto.setDuration(e.getDuration());
        dto.setPrize(e.getPrize());
        dto.setVenue(e.getVenue());
        dto.setEligibility(e.getEligibility());
        dto.setFee(e.getFee());
        dto.setRegistrationUrl(e.getRegistrationUrl());
        dto.setRegistrationDeadline(e.getRegistrationDeadline());
        dto.setIsPublished(e.getIsPublished());
        dto.setDisplayOrder(e.getDisplayOrder());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());

        dto.setHighlights(e.getHighlightsList());
        dto.setRules(e.getRulesList());
        dto.setCoordinators(e.getCoordinatorsList());

        if (e.getSessions() != null) {
            List<EventSessionDto> sDtos = new ArrayList<>();
            List<ScheduleSlotDto> slots = new ArrayList<>();
            for (EventSession s : e.getSessions()) {
                EventSessionDto sd = new EventSessionDto();
                sd.setId(s.getId());
                sd.setEventId(s.getEvent() != null ? s.getEvent().getId() : null);
                sd.setFestDay(s.getFestDay());
                sd.setStartTime(s.getStartTime());
                sd.setEndTime(s.getEndTime());
                sd.setSessionTitle(s.getSessionTitle());
                sd.setVenue(s.getVenue());
                sd.setDetails(s.getDetails());
                sDtos.add(sd);

                slots.add(new ScheduleSlotDto(s.getFestDay(), s.getTimeWindow(), s.getSessionTitle(), s.getVenue()));
            }
            dto.setSessions(sDtos);
            dto.setSchedule(slots);
        }
        return dto;
    }
}
