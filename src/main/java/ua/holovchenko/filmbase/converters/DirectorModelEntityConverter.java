package ua.holovchenko.filmbase.converters;

import ua.holovchenko.filmbase.entities.Director;
import ua.holovchenko.filmbase.models.DirectorModel;

import java.util.stream.Collectors;

import static ua.holovchenko.filmbase.converters.FilmModelEntityConverter.*;

public class DirectorModelEntityConverter {
    public static DirectorModel directorEntityToModel(Director director) {
        DirectorModel directorModel = new DirectorModel(director.getId(), director.getName());
        directorModel.setFilms(director.getFilms().stream()
                .map(it -> filmEntityToModel(it, directorModel))
                .collect(Collectors.toSet()));
        return directorModel;
    }

    public static Director directorModelToEntity(DirectorModel directorModel) {
        Director director = new Director(directorModel.getId(), directorModel.getName());
        director.setFilms(directorModel.getFilms().stream()
                .map(it -> filmModelToEntity(it, director))
                .collect(Collectors.toSet()));
        return director;
    }
}
