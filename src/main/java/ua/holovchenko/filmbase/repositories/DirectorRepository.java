package ua.holovchenko.filmbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.holovchenko.filmbase.entities.Director;

public interface DirectorRepository extends JpaRepository<Director, Long>, JpaSpecificationExecutor<Director> {

    boolean existsByName(String name);
}