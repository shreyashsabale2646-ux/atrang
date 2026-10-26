package in.ac.iiitt.atrang.controller;

import in.ac.iiitt.atrang.dto.ApiResponse;
import in.ac.iiitt.atrang.dto.FestDayDto;
import in.ac.iiitt.atrang.service.FestDayService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/fest-days")
public class AdminFestDayController {

    private final FestDayService festDayService;

    public AdminFestDayController(FestDayService festDayService) {
        this.festDayService = festDayService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FestDayDto>>> getFestDays() {
        return ResponseEntity.ok(ApiResponse.ok(festDayService.getAll()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<List<FestDayDto>>> replaceFestDays(
            @RequestBody List<FestDayDto> days,
            Authentication authentication) {
        String performer = authentication != null ? authentication.getName() : "ADMIN";
        return ResponseEntity.ok(ApiResponse.ok("Fest days updated", festDayService.replaceAll(days, performer)));
    }
}
