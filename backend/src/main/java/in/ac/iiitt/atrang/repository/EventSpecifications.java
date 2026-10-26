package in.ac.iiitt.atrang.repository;

import in.ac.iiitt.atrang.entity.Event;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EventSpecifications {

    public static Specification<Event> withAdminFilters(Long categoryId, Boolean isPublished, String search) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (isPublished != null) {
                predicates.add(cb.equal(root.get("isPublished"), isPublished));
            }

            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(root.get("summary")), pattern),
                        cb.like(cb.lower(root.get("slug")), pattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Event> withPublicFilters(String categoryCode, String search) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isTrue(root.get("isPublished")));
            predicates.add(cb.isTrue(root.get("category").get("isActive")));

            if (categoryCode != null && !categoryCode.trim().isEmpty()) {
                predicates.add(cb.or(
                        cb.equal(cb.lower(root.get("category").get("code")), categoryCode.trim().toLowerCase()),
                        cb.equal(cb.lower(root.get("category").get("name")), categoryCode.trim().toLowerCase())
                ));
            }

            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(root.get("summary")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
