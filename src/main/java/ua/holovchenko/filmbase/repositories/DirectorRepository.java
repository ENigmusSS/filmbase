package ua.holovchenko.filmbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ua.holovchenko.filmbase.entities.Director;

public interface DirectorRepository extends JpaRepository<Director, Long>, JpaSpecificationExecutor<Director> {

    boolean existsByName(String name);

    Director findByName(String directedBy);
}