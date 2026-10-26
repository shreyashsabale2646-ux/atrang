package in.ac.iiitt.atrang.controller;

import in.ac.iiitt.atrang.dto.ApiResponse;
import in.ac.iiitt.atrang.dto.CategoryDto;
import in.ac.iiitt.atrang.dto.EventDetailDto;
import in.ac.iiitt.atrang.dto.FestDayDto;
import in.ac.iiitt.atrang.service.CategoryService;
import in.ac.iiitt.atrang.service.EventService;
import in.ac.iiitt.atrang.service.FestDayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicEventController {

    private final EventService eventService;
    private final CategoryService categoryService;
    private final FestDayService festDayService;

    public PublicEventController(EventService eventService, CategoryService categoryService, FestDayService festDayService) {
        this.eventService = eventService;
        this.categoryService = categoryService;
        this.festDayService = festDayService;
    }

    @GetMapping("/events")
    public ResponseEntity<ApiResponse<List<EventDetailDto>>> getEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        List<EventDetailDto> events = eventService.getPublicEvents(category, search);
        return ResponseEntity.ok(ApiResponse.ok(events));
    }

    @GetMapping("/events/{slug}")
    public ResponseEntity<ApiResponse<EventDetailDto>> getEventBySlug(@PathVariable String slug) {
        EventDetailDto event = eventService.getPublicEventBySlug(slug);
        return ResponseEntity.ok(ApiResponse.ok(event));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategories() {
        List<CategoryDto> categories = categoryService.getPublicCategories();
        return ResponseEntity.ok(ApiResponse.ok(categories));
    }

    @GetMapping("/fest-days")
    public ResponseEntity<ApiResponse<List<FestDayDto>>> getFestDays() {
        return ResponseEntity.ok(ApiResponse.ok(festDayService.getAll()));
    }
}
