package in.ac.iiitt.atrang.controller;

import in.ac.iiitt.atrang.dto.ApiResponse;
import in.ac.iiitt.atrang.dto.AuditLogDto;
import in.ac.iiitt.atrang.dto.CategoryMetricDto;
import in.ac.iiitt.atrang.dto.DashboardSummaryDto;
import in.ac.iiitt.atrang.service.AuditService;
import in.ac.iiitt.atrang.service.DashboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {

    private final DashboardService dashboardService;
    private final AuditService auditService;

    public AdminDashboardController(DashboardService dashboardService, AuditService auditService) {
        this.dashboardService = dashboardService;
        this.auditService = auditService;
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryDto>> getSummary() {
        DashboardSummaryDto summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }

    @GetMapping("/reports/events-by-category")
    public ResponseEntity<ApiResponse<List<CategoryMetricDto>>> getEventsByCategory() {
        List<CategoryMetricDto> metrics = dashboardService.getCategoryMetrics();
        return ResponseEntity.ok(ApiResponse.ok(metrics));
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<ApiResponse<Page<AuditLogDto>>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AuditLogDto> logs = auditService.getAllLogs(PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.ok(logs));
    }
}
