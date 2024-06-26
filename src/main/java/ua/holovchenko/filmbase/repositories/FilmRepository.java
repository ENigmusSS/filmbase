package ua.holovchenko.filmbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.holovchenko.filmbase.entities.Film;

/**
 * Repository for Film entities.
 */
public interface FilmRepository extends JpaRepository<Film, Long>, JpaSpecificationExecutor<Film> {

    boolean existsByTitle(String title);
}