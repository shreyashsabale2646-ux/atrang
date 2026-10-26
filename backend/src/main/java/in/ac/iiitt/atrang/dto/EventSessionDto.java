package in.ac.iiitt.atrang.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EventSessionDto {
    private Long id;
    private Long eventId;
    private Integer festDay;
    private String startTime;
    private String endTime;
    private String sessionTitle;
    private String venue;
    private String details;

    public EventSessionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public Integer getFestDay() { return festDay; }
    public void setFestDay(Integer festDay) { this.festDay = festDay; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getSessionTitle() { return sessionTitle; }
    public void setSessionTitle(String sessionTitle) { this.sessionTitle = sessionTitle; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
