package ua.holovchenko.filmbase.converters;

import ua.holovchenko.filmbase.entities.Director;
import ua.holovchenko.filmbase.models.DirectorModel;

import java.util.stream.Collectors;

import static ua.holovchenko.filmbase.converters.FilmModelEntityConverter.*;

/**
 * Converter class to convert between Director entities and Director models.
 */
public class DirectorModelEntityConverter {

    /**
     * Converts a Director entity to a Director model.
     * @param director The Director entity to be converted.
     * @return The converted Director model.
     */
    public static DirectorModel directorEntityToModel(Director director) {
        DirectorModel directorModel = new DirectorModel(director.getId(), director.getName());
        directorModel.setFilms(director.getFilms().stream()
                .map(it -> filmEntityToModel(it, directorModel))
                .collect(Collectors.toSet()));
        return directorModel;
    }

    /**
     * Converts a Director model to a Director entity.
     * @param directorModel The Director model to be converted.
     * @return The converted Director entity.
     */
    public static Director directorModelToEntity(DirectorModel directorModel) {
        Director director = new Director(directorModel.getId(), directorModel.getName());
        director.setFilms(directorModel.getFilms().stream()
                .map(it -> filmModelToEntity(it, director))
                .collect(Collectors.toSet()));
        return director;
    }
}
