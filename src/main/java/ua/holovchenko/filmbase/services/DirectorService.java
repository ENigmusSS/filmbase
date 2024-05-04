package ua.holovchenko.filmbase.services;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.holovchenko.filmbase.entities.Director;
import ua.holovchenko.filmbase.models.DirectorModel;
import ua.holovchenko.filmbase.repositories.DirectorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static ua.holovchenko.filmbase.converters.DirectorModelEntityConverter.directorEntityToModel;
import static ua.holovchenko.filmbase.converters.DirectorModelEntityConverter.directorModelToEntity;

/**
 * Service class for Director entities.
 */
@Service
public class DirectorService {
    private final DirectorRepository repo;

    /**
     * Constructor for DirectorService.
     * @param repo The repository for Director entities.
     */
    @Autowired
    public DirectorService(DirectorRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves all directors.
     * @return A list of DirectorModel.
     */
    public List<DirectorModel> findAll() {
        List<DirectorModel> modelList = new ArrayList<>();
        for (Director director: repo.findAll()) {
            modelList.add(directorEntityToModel(director));
        }
        return modelList;
    }

    /**
     * Creates a new director.
     * @param model The DirectorModel representing the director to be created.
     * @return The created DirectorModel.
     * @throws ValidationException if the director already exists.
     */
    public DirectorModel createDirector(DirectorModel model) {
        if (repo.existsByName(model.getName())) {
            throw new ValidationException("Director already exists: " + model.getName());
        }
        return directorEntityToModel(repo.save(directorModelToEntity(model)));
    }

    /**
     * Updates an existing director.
     * @param id The id of the director to be updated.
     * @param model The DirectorModel representing the updated director.
     * @return The updated DirectorModel.
     * @throws ValidationException if the director with this name already exists.
     * @throws NoSuchElementException if the director with this ID does not exist.
     */
    public DirectorModel updateDirector(Long id, DirectorModel model) {
        if (repo.existsByName(model.getName())) {
            throw new ValidationException("Director already exists: " + model.getName());
        }
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Director does not exist: " + id);
        }
        repo.saveAndFlush(directorModelToEntity(model));
        return directorEntityToModel(repo.findById(id).get());
    }

    /**
     * Deletes a director.
     * @param id The id of the director to be deleted.
     * @throws NoSuchElementException if the director does not exist.
     */
    public void deleteDirector(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Director does not exist: " + id);
        }
        repo.deleteById(id);
    }
}
