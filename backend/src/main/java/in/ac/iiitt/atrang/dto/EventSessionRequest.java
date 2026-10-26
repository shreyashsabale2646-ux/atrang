package in.ac.iiitt.atrang.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EventSessionRequest {
    @NotNull(message = "Fest day is required")
    @Min(value = 1, message = "Day must be at least 1")
    @Max(value = 7, message = "Day cannot exceed 7")
    private Integer festDay = 1;

    @NotBlank(message = "Start time is required")
    private String startTime;

    @NotBlank(message = "End time is required")
    private String endTime;

    @NotBlank(message = "Session title is required")
    private String sessionTitle;

    private String venue;
    private String details;

    public EventSessionRequest() {}

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
