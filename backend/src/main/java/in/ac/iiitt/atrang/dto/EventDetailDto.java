package in.ac.iiitt.atrang.dto;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDetailDto {
    private Long id;
    private String slug;
    private String title;
    private String summary;
    private String description;
    private Long categoryId;
    private String categoryCode;
    private String categoryName;
    private String categoryColor;
    private String categoryIcon;
    private String organizer;
    private String participation;
    private String teamSize;
    private String duration;
    private String prize;
    private String venue;
    private String eligibility;
    private String fee;
    private String registrationUrl;
    private OffsetDateTime registrationDeadline;
    private Boolean isPublished;
    private Integer displayOrder;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private List<EventSessionDto> sessions = new ArrayList<>();
    private List<ScheduleSlotDto> schedule = new ArrayList<>();
    private List<String> highlights = new ArrayList<>();
    private List<String> rules = new ArrayList<>();
    private List<CoordinatorDto> coordinators = new ArrayList<>();

    public EventDetailDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCategoryColor() { return categoryColor; }
    public void setCategoryColor(String categoryColor) { this.categoryColor = categoryColor; }
    public String getCategoryIcon() { return categoryIcon; }
    public void setCategoryIcon(String categoryIcon) { this.categoryIcon = categoryIcon; }
    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) { this.organizer = organizer; }
    public String getParticipation() { return participation; }
    public void setParticipation(String participation) { this.participation = participation; }
    public String getTeamSize() { return teamSize; }
    public void setTeamSize(String teamSize) { this.teamSize = teamSize; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getPrize() { return prize; }
    public void setPrize(String prize) { this.prize = prize; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getEligibility() { return eligibility; }
    public void setEligibility(String eligibility) { this.eligibility = eligibility; }
    public String getFee() { return fee; }
    public void setFee(String fee) { this.fee = fee; }
    public String getRegistrationUrl() { return registrationUrl; }
    public void setRegistrationUrl(String registrationUrl) { this.registrationUrl = registrationUrl; }
    public OffsetDateTime getRegistrationDeadline() { return registrationDeadline; }
    public void setRegistrationDeadline(OffsetDateTime registrationDeadline) { this.registrationDeadline = registrationDeadline; }
    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<EventSessionDto> getSessions() { return sessions; }
    public void setSessions(List<EventSessionDto> sessions) { this.sessions = sessions; }

    public List<ScheduleSlotDto> getSchedule() { return schedule; }
    public void setSchedule(List<ScheduleSlotDto> schedule) { this.schedule = schedule; }

    public List<String> getHighlights() { return highlights; }
    public void setHighlights(List<String> highlights) { this.highlights = highlights; }

    public List<String> getRules() { return rules; }
    public void setRules(List<String> rules) { this.rules = rules; }

    public List<CoordinatorDto> getCoordinators() { return coordinators; }
    public void setCoordinators(List<CoordinatorDto> coordinators) { this.coordinators = coordinators; }
}
