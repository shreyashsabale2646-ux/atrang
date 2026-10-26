package in.ac.iiitt.atrang.repository;

import in.ac.iiitt.atrang.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findBySlug(String slug);

    Optional<Event> findBySlugAndIsPublishedTrue(String slug);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);

    long countByCategoryId(Long categoryId);

    long countByIsPublishedTrue();

    long countByIsPublishedFalse();

    @Query("SELECT e FROM Event e WHERE e.isPublished = true " +
           "AND (:categoryCode IS NULL OR LOWER(e.category.code) = LOWER(:categoryCode)) " +
           "AND (:search IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "     OR LOWER(e.summary) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY e.displayOrder ASC, e.id ASC")
    List<Event> findPublicEvents(
            @Param("categoryCode") String categoryCode,
            @Param("search") String search);

    @Query("SELECT e FROM Event e WHERE " +
           "(:categoryId IS NULL OR e.category.id = :categoryId) " +
           "AND (:isPublished IS NULL OR e.isPublished = :isPublished) " +
           "AND (:search IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "     OR LOWER(e.summary) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "     OR LOWER(e.slug) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY e.displayOrder ASC, e.id DESC")
    Page<Event> findAdminEvents(
            @Param("categoryId") Long categoryId,
            @Param("isPublished") Boolean isPublished,
            @Param("search") String search,
            Pageable pageable);
}
