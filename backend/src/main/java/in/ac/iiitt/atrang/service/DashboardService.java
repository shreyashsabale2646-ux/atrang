package in.ac.iiitt.atrang.service;

import in.ac.iiitt.atrang.dto.CategoryMetricDto;
import in.ac.iiitt.atrang.dto.DashboardSummaryDto;
import in.ac.iiitt.atrang.entity.Event;
import in.ac.iiitt.atrang.entity.EventCategory;
import in.ac.iiitt.atrang.repository.EventAuditLogRepository;
import in.ac.iiitt.atrang.repository.EventCategoryRepository;
import in.ac.iiitt.atrang.repository.EventRepository;
import in.ac.iiitt.atrang.repository.EventSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final EventRepository eventRepository;
    private final EventCategoryRepository categoryRepository;
    private final EventSessionRepository sessionRepository;
    private final EventAuditLogRepository auditLogRepository;

    public DashboardService(EventRepository eventRepository,
                            EventCategoryRepository categoryRepository,
                            EventSessionRepository sessionRepository,
                            EventAuditLogRepository auditLogRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.sessionRepository = sessionRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryDto getDashboardSummary() {
        long totalEvents = eventRepository.count();
        long publishedEvents = eventRepository.countByIsPublishedTrue();
        long draftEvents = eventRepository.countByIsPublishedFalse();
        long totalCategories = categoryRepository.count();
        long totalSessions = sessionRepository.count();
        long totalAuditLogs = auditLogRepository.count();

        List<CategoryMetricDto> metrics = getCategoryMetrics();

        return new DashboardSummaryDto(
                totalEvents,
                publishedEvents,
                draftEvents,
                totalCategories,
                totalSessions,
                totalAuditLogs,
                metrics
        );
    }

    @Transactional(readOnly = true)
    public List<CategoryMetricDto> getCategoryMetrics() {
        List<EventCategory> categories = categoryRepository.findAllByOrderByDisplayOrderAsc();
        List<Event> allEvents = eventRepository.findAll();

        List<CategoryMetricDto> list = new ArrayList<>();
        for (EventCategory cat : categories) {
            long total = allEvents.stream().filter(e -> e.getCategory().getId().equals(cat.getId())).count();
            long pub = allEvents.stream().filter(e -> e.getCategory().getId().equals(cat.getId()) && Boolean.TRUE.equals(e.getIsPublished())).count();
            long draft = total - pub;

            list.add(new CategoryMetricDto(
                    cat.getId(),
                    cat.getCode(),
                    cat.getName(),
                    cat.getColorHex(),
                    cat.getIconName(),
                    total,
                    pub,
                    draft
            ));
        }
        return list;
    }
}
