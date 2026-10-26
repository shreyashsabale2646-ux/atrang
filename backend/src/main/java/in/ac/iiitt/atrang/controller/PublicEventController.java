package in.ac.iiitt.atrang.controller;

import in.ac.iiitt.atrang.dto.ApiResponse;
import in.ac.iiitt.atrang.dto.CategoryDto;
import in.ac.iiitt.atrang.dto.EventDetailDto;
import in.ac.iiitt.atrang.dto.EventSummaryDto;
import in.ac.iiitt.atrang.service.CategoryService;
import in.ac.iiitt.atrang.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicEventController {

    private final EventService eventService;
    private final CategoryService categoryService;

    public PublicEventController(EventService eventService, CategoryService categoryService) {
        this.eventService = eventService;
        this.categoryService = categoryService;
    }

    @GetMapping("/events")
    public ResponseEntity<ApiResponse<List<EventSummaryDto>>> getEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        List<EventSummaryDto> events = eventService.getPublicEvents(category, search);
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
}
