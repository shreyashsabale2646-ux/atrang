package in.ac.iiitt.atrang.dto;

import java.time.OffsetDateTime;
import java.util.List;

/** Fields shared by {@link EventCreateRequest} and {@link EventUpdateRequest}. */
public interface EventPayload {
    Long getCategoryId();
    String getSlug();
    String getTitle();
    String getSummary();
    String getDescription();
    String getOrganizer();
    String getParticipation();
    String getTeamSize();
    String getDuration();
    String getPrize();
    String getVenue();
    String getEligibility();
    String getFee();
    String getRegistrationUrl();
    OffsetDateTime getRegistrationDeadline();
    Boolean getIsPublished();
    Integer getDisplayOrder();
    List<EventSessionRequest> getSessions();
    List<String> getHighlights();
    List<String> getRules();
    List<CoordinatorDto> getCoordinators();
}
