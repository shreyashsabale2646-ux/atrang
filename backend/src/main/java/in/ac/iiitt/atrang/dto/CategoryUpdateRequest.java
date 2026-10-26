package in.ac.iiitt.atrang.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CategoryUpdateRequest {

    @NotBlank(message = "Category name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    private String description;

    @Size(max = 100, message = "Biome name cannot exceed 100 characters")
    private String biomeName;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid 6-digit hex code")
    private String colorHex;

    private String iconName;

    private Integer displayOrder;

    private Boolean isActive;

    public CategoryUpdateRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBiomeName() { return biomeName; }
    public void setBiomeName(String biomeName) { this.biomeName = biomeName; }

    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }

    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }

    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
