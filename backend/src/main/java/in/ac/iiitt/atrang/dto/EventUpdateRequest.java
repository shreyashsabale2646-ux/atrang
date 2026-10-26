package in.ac.iiitt.atrang.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventUpdateRequest {

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Slug is required")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug must contain only lower-case alphanumeric characters and hyphens")
    private String slug;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Summary is required")
    @Size(max = 300, message = "Summary cannot exceed 300 characters")
    private String summary;

    @NotBlank(message = "Description is required")
    private String description;

    private String organizer;

    @Pattern(regexp = "^(Individual|Team|Individual or Team)$", message = "Participation must be 'Individual', 'Team', or 'Individual or Team'")
    private String participation;

    private String teamSize;
    private String duration;
    private String prize;

    @NotBlank(message = "Venue is required")
    private String venue;

    private String eligibility;
    private String fee;
    private String registrationUrl;
    private OffsetDateTime registrationDeadline;
    private Boolean isPublished;
    private Integer displayOrder;

    @Valid
    private List<EventSessionRequest> sessions = new ArrayList<>();

    public EventUpdateRequest() {}

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
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
    public List<EventSessionRequest> getSessions() { return sessions; }
    public void setSessions(List<EventSessionRequest> sessions) { this.sessions = sessions; }
}
