package in.ac.iiitt.atrang.repository;

import in.ac.iiitt.atrang.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Optional<Event> findBySlug(String slug);

    Optional<Event> findBySlugAndIsPublishedTrue(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    long countByCategoryId(Long categoryId);

    long countByIsPublishedTrue();

    long countByIsPublishedFalse();
}
