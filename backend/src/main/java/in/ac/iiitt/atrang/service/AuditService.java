package in.ac.iiitt.atrang.service;

import in.ac.iiitt.atrang.dto.AuditLogDto;
import in.ac.iiitt.atrang.entity.EventAuditLog;
import in.ac.iiitt.atrang.repository.EventAuditLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditService {

    private final EventAuditLogRepository auditLogRepository;

    public AuditService(EventAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void log(Long eventId, Long adminId, String action, String details, String performedBy, String ipAddress) {
        EventAuditLog log = new EventAuditLog(eventId, adminId, action, details, performedBy, ipAddress);
        auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<AuditLogDto> getRecentLogs() {
        return auditLogRepository.findTop20ByOrderByCreatedAtDesc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<AuditLogDto> getAllLogs(Pageable pageable) {
        return auditLogRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::mapToDto);
    }

    private AuditLogDto mapToDto(EventAuditLog entity) {
        return new AuditLogDto(
                entity.getId(),
                entity.getEventId(),
                entity.getAdminId(),
                entity.getAction(),
                entity.getDetails(),
                entity.getPerformedBy(),
                entity.getIpAddress(),
                entity.getCreatedAt()
        );
    }
}
