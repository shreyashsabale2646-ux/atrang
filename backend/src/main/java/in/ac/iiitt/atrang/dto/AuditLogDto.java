package in.ac.iiitt.atrang.dto;

import java.time.OffsetDateTime;

public class AuditLogDto {
    private Long id;
    private Long eventId;
    private Long adminId;
    private String action;
    private String details;
    private String performedBy;
    private String ipAddress;
    private OffsetDateTime createdAt;

    public AuditLogDto() {}

    public AuditLogDto(Long id, Long eventId, Long adminId, String action, String details, String performedBy, String ipAddress, OffsetDateTime createdAt) {
        this.id = id;
        this.eventId = eventId;
        this.adminId = adminId;
        this.action = action;
        this.details = details;
        this.performedBy = performedBy;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
