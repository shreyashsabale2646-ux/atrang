package in.ac.iiitt.atrang.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fest_days")
public class FestDay {

    @Id
    @Column(name = "day_number")
    private Integer dayNumber;

    @Column(name = "fest_date", nullable = false, unique = true)
    private LocalDate festDate;

    public FestDay() {}

    public FestDay(Integer dayNumber, LocalDate festDate) {
        this.dayNumber = dayNumber;
        this.festDate = festDate;
    }

    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }

    public LocalDate getFestDate() { return festDate; }
    public void setFestDate(LocalDate festDate) { this.festDate = festDate; }
}
