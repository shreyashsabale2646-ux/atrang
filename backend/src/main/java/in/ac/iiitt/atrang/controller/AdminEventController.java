package in.ac.iiitt.atrang.controller;

import in.ac.iiitt.atrang.dto.*;
import in.ac.iiitt.atrang.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/events")
public class AdminEventController {

    private final EventService eventService;

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EventSummaryDto>>> getEvents(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean isPublished,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EventSummaryDto> result = eventService.getAdminEvents(categoryId, isPublished, search, pageable);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDetailDto>> getEventById(@PathVariable Long id) {
        EventDetailDto event = eventService.getAdminEventById(id);
        return ResponseEntity.ok(ApiResponse.ok(event));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventDetailDto>> createEvent(
            @Valid @RequestBody EventCreateRequest request,
            Authentication authentication) {
        String performer = authentication != null ? authentication.getName() : "ADMIN";
        EventDetailDto created = eventService.createEvent(request, performer);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Event created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventDetailDto>> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventUpdateRequest request,
            Authentication authentication) {
        String performer = authentication != null ? authentication.getName() : "ADMIN";
        EventDetailDto updated = eventService.updateEvent(id, request, performer);
        return ResponseEntity.ok(ApiResponse.ok("Event updated successfully", updated));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<EventDetailDto>> togglePublication(
            @PathVariable Long id,
            Authentication authentication) {
        String performer = authentication != null ? authentication.getName() : "ADMIN";
        EventDetailDto updated = eventService.togglePublication(id, performer);
        return ResponseEntity.ok(ApiResponse.ok("Publication status toggled", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEvent(
            @PathVariable Long id,
            Authentication authentication) {
        String performer = authentication != null ? authentication.getName() : "ADMIN";
        eventService.deleteEvent(id, performer);
        return ResponseEntity.ok(ApiResponse.ok("Event deleted successfully", "Deleted event id: " + id));
    }
}
