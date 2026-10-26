package in.ac.iiitt.atrang.dto;

import java.time.LocalDate;

public class FestDayDto {
    private Integer day;
    private LocalDate date;

    public FestDayDto() {}

    public FestDayDto(Integer day, LocalDate date) {
        this.day = day;
        this.date = date;
    }

    public Integer getDay() { return day; }
    public void setDay(Integer day) { this.day = day; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
