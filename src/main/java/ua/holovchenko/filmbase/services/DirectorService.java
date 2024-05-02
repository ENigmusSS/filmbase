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

@Service
public class DirectorService {
    private final DirectorRepository repo;
    @Autowired
    public DirectorService(DirectorRepository repo) {
        this.repo = repo;
    }


    public List<DirectorModel> findAll() {
        List<DirectorModel> modelList = new ArrayList<>();
        for (Director director:
                repo.findAll()) {
            modelList.add(directorEntityToModel(director));
        }
        return modelList;
    }

    public DirectorModel createDirector(DirectorModel model) {
        if (repo.existsById(model.getId())) {
            throw new ValidationException("Director already exists: " + model.getName());
        }
        return directorEntityToModel(repo.save(directorModelToEntity(model)));
    }

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

    public void deleteDirector(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Director does not exist: " + id);
        }
        repo.deleteById(id);
    }
}
