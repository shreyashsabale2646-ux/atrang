package in.ac.iiitt.atrang.dto;

import java.time.OffsetDateTime;

public class CategoryDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String biomeName;
    private String colorHex;
    private String iconName;
    private Integer displayOrder;
    private Boolean isActive;
    private Long eventCount;
    private OffsetDateTime createdAt;

    public CategoryDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
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
    public Long getEventCount() { return eventCount; }
    public void setEventCount(Long eventCount) { this.eventCount = eventCount; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
