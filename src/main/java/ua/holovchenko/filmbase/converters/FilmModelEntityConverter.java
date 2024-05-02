package ua.holovchenko.filmbase.converters;

import ua.holovchenko.filmbase.entities.Director;
import ua.holovchenko.filmbase.entities.Film;
import ua.holovchenko.filmbase.models.DirectorModel;
import ua.holovchenko.filmbase.models.FilmModel;

import java.util.Arrays;
import java.util.stream.Collectors;

import static ua.holovchenko.filmbase.converters.DirectorModelEntityConverter.directorEntityToModel;
import static ua.holovchenko.filmbase.converters.DirectorModelEntityConverter.directorModelToEntity;

public class FilmModelEntityConverter {

    public static final java.util.function.Supplier<String> STRING_UNKNOWN = () -> "Unknown";

    public static FilmModel filmEntityToModel(Film film) {
        return new FilmModel(
                film.getId(),
                film.getTitle(),
                film.getYear(),
                directorEntityToModel(film.getDirectedBy()),
                Arrays.stream(film.getWrittenBy().split(", ")).collect(Collectors.toSet()),
                Arrays.stream(film.getProducedBy().split(", ")).collect(Collectors.toSet()),
                Arrays.stream(film.getStarring().split(", ")).collect(Collectors.toSet()),
                film.getRunningTime(),
                Arrays.stream(film.getGenres().split(", ")).collect(Collectors.toSet())
        );
    }

    public static Film filmModelToEntity(FilmModel filmModel) {
        return new Film(
                filmModel.getId(),
                filmModel.getTitle(),
                filmModel.getYear(),
                directorModelToEntity(filmModel.getDirectedBy()),
                filmModel.getWrittenBy().stream().reduce((s, s2) -> s2 + ", " + s).orElseGet(STRING_UNKNOWN),
                filmModel.getProducedBy().stream().reduce((s, s2) -> s2 + ", " + s).orElseGet(STRING_UNKNOWN),
                filmModel.getStarring().stream().reduce((s, s2) -> s2 + ", " + s).orElseGet(STRING_UNKNOWN),
                filmModel.getRunningTime(),
                filmModel.getGenres().stream().reduce((s, s2) -> s2 + ", " + s).orElseGet(STRING_UNKNOWN)
        );
    }

    public static FilmModel filmEntityToModel(Film film, DirectorModel directorModel) {
        return new FilmModel(
                film.getId(),
                film.getTitle(),
                film.getYear(),
                directorModel,
                Arrays.stream(film.getWrittenBy().split(", ")).collect(Collectors.toSet()),
                Arrays.stream(film.getProducedBy().split(", ")).collect(Collectors.toSet()),
                Arrays.stream(film.getStarring().split(", ")).collect(Collectors.toSet()),
                film.getRunningTime(),
                Arrays.stream(film.getGenres().split(", ")).collect(Collectors.toSet())
        );
    }

    public static Film filmModelToEntity(FilmModel filmModel, Director director) {
        return new Film(
                filmModel.getId(),
                filmModel.getTitle(),
                filmModel.getYear(),
                director,
                filmModel.getWrittenBy().stream().reduce((s, s2) -> s2 + ", " + s).orElseGet(STRING_UNKNOWN),
                filmModel.getProducedBy().stream().reduce((s, s2) -> s2 + ", " + s).orElseGet(STRING_UNKNOWN),
                filmModel.getStarring().stream().reduce((s, s2) -> s2 + ", " + s).orElseGet(STRING_UNKNOWN),
                filmModel.getRunningTime(),
                filmModel.getGenres().stream().reduce((s, s2) -> s2 + ", " + s).orElseGet(STRING_UNKNOWN)
        );
    }
}

