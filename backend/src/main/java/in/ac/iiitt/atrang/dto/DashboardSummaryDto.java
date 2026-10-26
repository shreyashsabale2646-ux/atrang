package in.ac.iiitt.atrang.dto;

import java.util.List;

public class DashboardSummaryDto {
    private long totalEvents;
    private long publishedEvents;
    private long draftEvents;
    private long totalCategories;
    private long totalSessions;
    private long totalAuditLogs;
    private List<CategoryMetricDto> categoryMetrics;

    public DashboardSummaryDto() {}

    public DashboardSummaryDto(long totalEvents, long publishedEvents, long draftEvents, long totalCategories, long totalSessions, long totalAuditLogs, List<CategoryMetricDto> categoryMetrics) {
        this.totalEvents = totalEvents;
        this.publishedEvents = publishedEvents;
        this.draftEvents = draftEvents;
        this.totalCategories = totalCategories;
        this.totalSessions = totalSessions;
        this.totalAuditLogs = totalAuditLogs;
        this.categoryMetrics = categoryMetrics;
    }

    public long getTotalEvents() { return totalEvents; }
    public void setTotalEvents(long totalEvents) { this.totalEvents = totalEvents; }
    public long getPublishedEvents() { return publishedEvents; }
    public void setPublishedEvents(long publishedEvents) { this.publishedEvents = publishedEvents; }
    public long getDraftEvents() { return draftEvents; }
    public void setDraftEvents(long draftEvents) { this.draftEvents = draftEvents; }
    public long getTotalCategories() { return totalCategories; }
    public void setTotalCategories(long totalCategories) { this.totalCategories = totalCategories; }
    public long getTotalSessions() { return totalSessions; }
    public void setTotalSessions(long totalSessions) { this.totalSessions = totalSessions; }
    public long getTotalAuditLogs() { return totalAuditLogs; }
    public void setTotalAuditLogs(long totalAuditLogs) { this.totalAuditLogs = totalAuditLogs; }
    public List<CategoryMetricDto> getCategoryMetrics() { return categoryMetrics; }
    public void setCategoryMetrics(List<CategoryMetricDto> categoryMetrics) { this.categoryMetrics = categoryMetrics; }
}
