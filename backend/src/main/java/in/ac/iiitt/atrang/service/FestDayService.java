package in.ac.iiitt.atrang.service;

import in.ac.iiitt.atrang.dto.FestDayDto;
import in.ac.iiitt.atrang.entity.FestDay;
import in.ac.iiitt.atrang.exception.BadRequestException;
import in.ac.iiitt.atrang.repository.FestDayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FestDayService {

    private final FestDayRepository festDayRepository;
    private final AuditService auditService;

    public FestDayService(FestDayRepository festDayRepository, AuditService auditService) {
        this.festDayRepository = festDayRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<FestDayDto> getAll() {
        return festDayRepository.findAllByOrderByDayNumberAsc().stream()
                .map(d -> new FestDayDto(d.getDayNumber(), d.getFestDate()))
                .collect(Collectors.toList());
    }

    /** Replaces the whole fest calendar with {@code days}. */
    @Transactional
    public List<FestDayDto> replaceAll(List<FestDayDto> days, String performer) {
        Set<Integer> numbers = new HashSet<>();
        Set<LocalDate> dates = new HashSet<>();
        for (FestDayDto d : days) {
            if (d.getDay() == null || d.getDay() < 1 || d.getDay() > 7) throw new BadRequestException("Day numbers must be between 1 and 7");
            if (d.getDate() == null) throw new BadRequestException("Day " + d.getDay() + " needs a date");
            if (!numbers.add(d.getDay())) throw new BadRequestException("Day " + d.getDay() + " is listed twice");
            if (!dates.add(d.getDate())) throw new BadRequestException("Date " + d.getDate() + " is used by more than one day");
        }

        festDayRepository.deleteAllInBatch();
        festDayRepository.flush();
        festDayRepository.saveAll(days.stream()
                .map(d -> new FestDay(d.getDay(), d.getDate()))
                .collect(Collectors.toList()));

        auditService.log(null, null, "UPDATE", "Updated fest calendar (" + days.size() + " days)", performer, null);
        return getAll();
    }
}
