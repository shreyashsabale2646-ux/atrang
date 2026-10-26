package in.ac.iiitt.atrang.dto;

public class ScheduleSlotDto {
    private Integer day;
    private String time;
    private String title;
    private String venue;

    public ScheduleSlotDto() {}

    public ScheduleSlotDto(Integer day, String time, String title, String venue) {
        this.day = day;
        this.time = time;
        this.title = title;
        this.venue = venue;
    }

    public Integer getDay() { return day; }
    public void setDay(Integer day) { this.day = day; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
}
