package in.ac.iiitt.atrang.repository;

import in.ac.iiitt.atrang.entity.EventAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventAuditLogRepository extends JpaRepository<EventAuditLog, Long> {
    List<EventAuditLog> findTop20ByOrderByCreatedAtDesc();
    Page<EventAuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
