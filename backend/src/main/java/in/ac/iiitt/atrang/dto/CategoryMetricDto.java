package in.ac.iiitt.atrang.dto;

public class CategoryMetricDto {
    private Long categoryId;
    private String categoryCode;
    private String categoryName;
    private String colorHex;
    private String iconName;
    private long totalEvents;
    private long publishedEvents;
    private long draftEvents;

    public CategoryMetricDto() {}

    public CategoryMetricDto(Long categoryId, String categoryCode, String categoryName, String colorHex, String iconName, long totalEvents, long publishedEvents, long draftEvents) {
        this.categoryId = categoryId;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.colorHex = colorHex;
        this.iconName = iconName;
        this.totalEvents = totalEvents;
        this.publishedEvents = publishedEvents;
        this.draftEvents = draftEvents;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getCategoryCode() { return categoryCode; }
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getColorHex() { return colorHex; }
    public void setColorHex(String colorHex) { this.colorHex = colorHex; }
    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }
    public long getTotalEvents() { return totalEvents; }
    public void setTotalEvents(long totalEvents) { this.totalEvents = totalEvents; }
    public long getPublishedEvents() { return publishedEvents; }
    public void setPublishedEvents(long publishedEvents) { this.publishedEvents = publishedEvents; }
    public long getDraftEvents() { return draftEvents; }
    public void setDraftEvents(long draftEvents) { this.draftEvents = draftEvents; }
}
