package in.ac.iiitt.atrang.repository;

import in.ac.iiitt.atrang.entity.FestDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FestDayRepository extends JpaRepository<FestDay, Integer> {
    List<FestDay> findAllByOrderByDayNumberAsc();
}
