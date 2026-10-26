package in.ac.iiitt.atrang.repository;

import in.ac.iiitt.atrang.entity.EventSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventSessionRepository extends JpaRepository<EventSession, Long> {
    List<EventSession> findByEventIdOrderByFestDayAscStartTimeAsc(Long eventId);
    void deleteByEventId(Long eventId);
}
