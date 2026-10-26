package in.ac.iiitt.atrang.service;

import in.ac.iiitt.atrang.dto.CategoryCreateRequest;
import in.ac.iiitt.atrang.dto.CategoryDto;
import in.ac.iiitt.atrang.dto.CategoryUpdateRequest;
import in.ac.iiitt.atrang.entity.EventCategory;
import in.ac.iiitt.atrang.exception.BadRequestException;
import in.ac.iiitt.atrang.exception.ConflictException;
import in.ac.iiitt.atrang.exception.ResourceNotFoundException;
import in.ac.iiitt.atrang.repository.EventCategoryRepository;
import in.ac.iiitt.atrang.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final EventCategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final AuditService auditService;

    public CategoryService(EventCategoryRepository categoryRepository,
                           EventRepository eventRepository,
                           AuditService auditService) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getPublicCategories() {
        return categoryRepository.findAllByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        EventCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return mapToDto(category);
    }

    @Transactional
    public CategoryDto createCategory(CategoryCreateRequest req, String performer) {
        if (categoryRepository.existsByCode(req.getCode())) {
            throw new ConflictException("Category code already exists: " + req.getCode());
        }
        if (categoryRepository.existsByName(req.getName())) {
            throw new ConflictException("Category name already exists: " + req.getName());
        }

        EventCategory cat = new EventCategory(
                req.getCode().toLowerCase().trim(),
                req.getName().trim(),
                req.getDescription(),
                req.getColorHex(),
                req.getIconName(),
                req.getDisplayOrder()
        );

        EventCategory saved = categoryRepository.save(cat);
        auditService.log(null, null, "CREATE", "Created category: " + saved.getName(), performer, null);
        return mapToDto(saved);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryUpdateRequest req, String performer) {
        EventCategory cat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        if (!cat.getName().equalsIgnoreCase(req.getName()) && categoryRepository.existsByName(req.getName())) {
            throw new ConflictException("Category name already exists: " + req.getName());
        }

        cat.setName(req.getName().trim());
        if (req.getDescription() != null) cat.setDescription(req.getDescription());
        if (req.getColorHex() != null) cat.setColorHex(req.getColorHex());
        if (req.getIconName() != null) cat.setIconName(req.getIconName());
        if (req.getDisplayOrder() != null) cat.setDisplayOrder(req.getDisplayOrder());
        if (req.getIsActive() != null) cat.setIsActive(req.getIsActive());

        EventCategory saved = categoryRepository.save(cat);
        auditService.log(null, null, "UPDATE", "Updated category: " + saved.getName(), performer, null);
        return mapToDto(saved);
    }

    @Transactional
    public void deleteCategory(Long id, String performer) {
        EventCategory cat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        long eventCount = eventRepository.countByCategoryId(id);
        if (eventCount > 0) {
            throw new BadRequestException("Cannot delete category containing " + eventCount + " events. Reassign or delete events first.");
        }

        categoryRepository.delete(cat);
        auditService.log(null, null, "DELETE", "Deleted category: " + cat.getName(), performer, null);
    }

    private CategoryDto mapToDto(EventCategory cat) {
        CategoryDto dto = new CategoryDto();
        dto.setId(cat.getId());
        dto.setCode(cat.getCode());
        dto.setName(cat.getName());
        dto.setDescription(cat.getDescription());
        dto.setColorHex(cat.getColorHex());
        dto.setIconName(cat.getIconName());
        dto.setDisplayOrder(cat.getDisplayOrder());
        dto.setIsActive(cat.getIsActive());
        dto.setEventCount(eventRepository.countByCategoryId(cat.getId()));
        dto.setCreatedAt(cat.getCreatedAt());
        return dto;
    }
}
