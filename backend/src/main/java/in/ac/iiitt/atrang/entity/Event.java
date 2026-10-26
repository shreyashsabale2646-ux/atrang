package in.ac.iiitt.atrang.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.ac.iiitt.atrang.dto.CoordinatorDto;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "events")
public class Event {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private EventCategory category;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 300)
    private String summary;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 100)
    private String organizer = "";

    @Column(nullable = false, length = 30)
    private String participation = "Individual";

    @Column(name = "team_size", length = 50)
    private String teamSize;

    @Column(length = 50)
    private String duration;

    @Column(length = 100)
    private String prize;

    @Column(nullable = false, length = 100)
    private String venue;

    @Column(length = 150)
    private String eligibility;

    @Column(length = 50)
    private String fee;

    @Column(name = "registration_url", columnDefinition = "TEXT")
    private String registrationUrl;

    @Column(name = "registration_deadline")
    private OffsetDateTime registrationDeadline;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(columnDefinition = "TEXT")
    private String highlights;

    @Column(columnDefinition = "TEXT")
    private String rules;

    @Column(columnDefinition = "TEXT")
    private String coordinators;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("festDay ASC, startTime ASC")
    private List<EventSession> sessions = new ArrayList<>();

    public Event() {}

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public List<String> getHighlightsList() {
        if (highlights == null || highlights.trim().isEmpty()) return new ArrayList<>();
        try {
            return MAPPER.readValue(highlights, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Arrays.stream(highlights.split("\n")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        }
    }

    public void setHighlightsList(List<String> list) {
        if (list == null) { this.highlights = null; return; }
        try {
            this.highlights = MAPPER.writeValueAsString(list);
        } catch (Exception e) {
            this.highlights = String.join("\n", list);
        }
    }

    public List<String> getRulesList() {
        if (rules == null || rules.trim().isEmpty()) return new ArrayList<>();
        try {
            return MAPPER.readValue(rules, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Arrays.stream(rules.split("\n")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        }
    }

    public void setRulesList(List<String> list) {
        if (list == null) { this.rules = null; return; }
        try {
            this.rules = MAPPER.writeValueAsString(list);
        } catch (Exception e) {
            this.rules = String.join("\n", list);
        }
    }

    public List<CoordinatorDto> getCoordinatorsList() {
        if (coordinators == null || coordinators.trim().isEmpty()) return new ArrayList<>();
        try {
            return MAPPER.readValue(coordinators, new TypeReference<List<CoordinatorDto>>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void setCoordinatorsList(List<CoordinatorDto> list) {
        if (list == null) { this.coordinators = null; return; }
        try {
            this.coordinators = MAPPER.writeValueAsString(list);
        } catch (Exception e) {
            this.coordinators = null;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EventCategory getCategory() { return category; }
    public void setCategory(EventCategory category) { this.category = category; }

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

    public String getHighlights() { return highlights; }
    public void setHighlights(String highlights) { this.highlights = highlights; }

    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }

    public String getCoordinators() { return coordinators; }
    public void setCoordinators(String coordinators) { this.coordinators = coordinators; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<EventSession> getSessions() { return sessions; }
    public void setSessions(List<EventSession> sessions) { this.sessions = sessions; }
}
