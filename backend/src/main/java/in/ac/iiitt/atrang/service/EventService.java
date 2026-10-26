package in.ac.iiitt.atrang.service;

import in.ac.iiitt.atrang.dto.*;
import in.ac.iiitt.atrang.entity.Event;
import in.ac.iiitt.atrang.entity.EventCategory;
import in.ac.iiitt.atrang.entity.EventSession;
import in.ac.iiitt.atrang.exception.ConflictException;
import in.ac.iiitt.atrang.exception.ResourceNotFoundException;
import in.ac.iiitt.atrang.repository.EventCategoryRepository;
import in.ac.iiitt.atrang.repository.EventRepository;
import in.ac.iiitt.atrang.repository.EventSessionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventCategoryRepository categoryRepository;
    private final EventSessionRepository sessionRepository;
    private final AuditService auditService;

    public EventService(EventRepository eventRepository,
                        EventCategoryRepository categoryRepository,
                        EventSessionRepository sessionRepository,
                        AuditService auditService) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.sessionRepository = sessionRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<EventSummaryDto> getPublicEvents(String categoryCode, String search) {
        String cleanCategory = (categoryCode != null && !categoryCode.trim().isEmpty()) ? categoryCode.trim() : null;
        String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        return eventRepository.findPublicEvents(cleanCategory, cleanSearch).stream()
                .map(this::mapToSummaryDto)
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
        String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        return eventRepository.findAdminEvents(categoryId, isPublished, cleanSearch, pageable)
                .map(this::mapToSummaryDto);
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

        if (req.getSessions() != null && !req.getSessions().isEmpty()) {
            for (EventSessionRequest sReq : req.getSessions()) {
                EventSession session = new EventSession(
                        saved,
                        sReq.getFestDay(),
                        sReq.getStartTime(),
                        sReq.getEndTime(),
                        sReq.getSessionTitle(),
                        sReq.getVenue(),
                        sReq.getDetails()
                );
                sessionRepository.save(session);
                saved.getSessions().add(session);
            }
        }

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
        event.setCategory(category);
        event.setSlug(req.getSlug().trim().toLowerCase());
        event.setTitle(req.getTitle().trim());
        event.setSummary(req.getSummary().trim());
        event.setDescription(req.getDescription());
        if (req.getOrganizer() != null) event.setOrganizer(req.getOrganizer());
        if (req.getParticipation() != null) event.setParticipation(req.getParticipation());
        event.setTeamSize(req.getTeamSize());
        event.setDuration(req.getDuration());
        event.setPrize(req.getPrize());
        event.setVenue(req.getVenue());
        if (req.getEligibility() != null) event.setEligibility(req.getEligibility());
        if (req.getFee() != null) event.setFee(req.getFee());
        event.setRegistrationUrl(req.getRegistrationUrl());
        event.setRegistrationDeadline(req.getRegistrationDeadline());
        if (req.getIsPublished() != null) event.setIsPublished(req.getIsPublished());
        if (req.getDisplayOrder() != null) event.setDisplayOrder(req.getDisplayOrder());

        // Update sessions
        sessionRepository.deleteByEventId(event.getId());
        event.getSessions().clear();
        if (req.getSessions() != null) {
            for (EventSessionRequest sReq : req.getSessions()) {
                EventSession session = new EventSession(
                        event,
                        sReq.getFestDay(),
                        sReq.getStartTime(),
                        sReq.getEndTime(),
                        sReq.getSessionTitle(),
                        sReq.getVenue(),
                        sReq.getDetails()
                );
                sessionRepository.save(session);
                event.getSessions().add(session);
            }
        }

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

    private void applyEventFields(Event event, EventCategory category, EventCreateRequest req) {
        event.setCategory(category);
        event.setSlug(req.getSlug().trim().toLowerCase());
        event.setTitle(req.getTitle().trim());
        event.setSummary(req.getSummary().trim());
        event.setDescription(req.getDescription());
        if (req.getOrganizer() != null) event.setOrganizer(req.getOrganizer());
        if (req.getParticipation() != null) event.setParticipation(req.getParticipation());
        event.setTeamSize(req.getTeamSize());
        event.setDuration(req.getDuration());
        event.setPrize(req.getPrize());
        event.setVenue(req.getVenue());
        if (req.getEligibility() != null) event.setEligibility(req.getEligibility());
        if (req.getFee() != null) event.setFee(req.getFee());
        event.setRegistrationUrl(req.getRegistrationUrl());
        event.setRegistrationDeadline(req.getRegistrationDeadline());
        if (req.getIsPublished() != null) event.setIsPublished(req.getIsPublished());
        if (req.getDisplayOrder() != null) event.setDisplayOrder(req.getDisplayOrder());
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

        if (e.getSessions() != null) {
            List<EventSessionDto> sDtos = e.getSessions().stream().map(s -> {
                EventSessionDto sd = new EventSessionDto();
                sd.setId(s.getId());
                sd.setEventId(s.getEvent() != null ? s.getEvent().getId() : null);
                sd.setFestDay(s.getFestDay());
                sd.setStartTime(s.getStartTime());
                sd.setEndTime(s.getEndTime());
                sd.setSessionTitle(s.getSessionTitle());
                sd.setVenue(s.getVenue());
                sd.setDetails(s.getDetails());
                return sd;
            }).collect(Collectors.toList());
            dto.setSessions(sDtos);
        }
        return dto;
    }
}
