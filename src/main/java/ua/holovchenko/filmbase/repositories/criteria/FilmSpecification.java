package ua.holovchenko.filmbase.repositories.criteria;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ua.holovchenko.filmbase.controllers.dto.Filters;
import ua.holovchenko.filmbase.entities.Film;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification for Film filtering queries.
 * I don't really like that "ifs-battery", but the only simplifying way I see is reflection that is not recommended,
 * or unnecessary complexity with additional Filter dto
 */
@Component
public class FilmSpecification {
    public Specification<Film> filmSpecification(Filters filters) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filters.getWrittenBy() != null) {
                filters.getWrittenBy().forEach(s -> predicates.add(
                        criteriaBuilder.like(root.get("writtenBy"), "%" + s + "%")));
            }
            if (filters.getProducedBy() != null) {
                filters.getProducedBy().forEach(s -> predicates.add(
                        criteriaBuilder.like(root.get("producedBy"), "%" + s + "%")));
            }
            if (filters.getStarring() != null) {
                filters.getStarring().forEach(s -> predicates.add(
                        criteriaBuilder.like(root.get("starring"), "%" + s + "%")));
            }
            if (filters.getGenres() != null) {
                filters.getGenres().forEach(s -> predicates.add(
                        criteriaBuilder.like(root.get("genres"), "%" + s + "%")));
            }
            if (filters.getDirectedBy() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("directedBy"), filters.getDirectedBy())
                );
            }
            if (filters.getRunningTime() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("runningTime"), filters.getRunningTime())
                );
            }
            if (filters.getRunningTimeMin() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("runningTime"), filters.getRunningTimeMin())
                );
            }
            if (filters.getRunningTimeMax() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("runningTime"), filters.getRunningTimeMax())
                );
            }
            if (filters.getYear() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("year"), filters.getYear())
                );
            }
            if (filters.getYearSince() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("year"), filters.getYearSince())
                );
            }
            if (filters.getYearTo() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("year"), filters.getYearTo())
                );
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }
}

