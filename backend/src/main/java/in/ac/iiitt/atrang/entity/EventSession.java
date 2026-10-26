package in.ac.iiitt.atrang.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "event_sessions")
public class EventSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "fest_day", nullable = false)
    private Integer festDay = 1;

    @Column(name = "start_time", length = 50)
    private String startTime;

    @Column(name = "end_time", length = 50)
    private String endTime;

    @Column(name = "time_window", length = 100)
    private String timeWindow;

    @Column(name = "session_title", nullable = false, length = 150)
    private String sessionTitle;

    @Column(length = 100)
    private String venue;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public EventSession() {}

    public EventSession(Event event, Integer festDay, String startTime, String endTime, String sessionTitle, String venue, String details) {
        this.event = event;
        this.festDay = festDay;
        this.startTime = blankToNull(startTime);
        this.endTime = blankToNull(endTime);
        this.timeWindow = this.endTime != null ? this.startTime + " – " + this.endTime : this.startTime;
        this.sessionTitle = sessionTitle;
        this.venue = blankToNull(venue);
        this.details = blankToNull(details);
        this.createdAt = OffsetDateTime.now();
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public Integer getFestDay() { return festDay; }
    public void setFestDay(Integer festDay) { this.festDay = festDay; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getTimeWindow() {
        if (timeWindow != null && !timeWindow.isEmpty()) return timeWindow;
        if (startTime != null && endTime != null && !endTime.isEmpty()) return startTime + " – " + endTime;
        return startTime != null && !startTime.isEmpty() ? startTime : "All day";
    }
    public void setTimeWindow(String timeWindow) { this.timeWindow = timeWindow; }

    public String getSessionTitle() { return sessionTitle; }
    public void setSessionTitle(String sessionTitle) { this.sessionTitle = sessionTitle; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
