package in.ac.iiitt.atrang.repository;

import in.ac.iiitt.atrang.entity.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    Optional<EventCategory> findByCode(String code);
    Optional<EventCategory> findByName(String name);
    boolean existsByCode(String code);
    boolean existsByName(String name);
    List<EventCategory> findAllByOrderByDisplayOrderAsc();
    List<EventCategory> findAllByIsActiveTrueOrderByDisplayOrderAsc();
}
