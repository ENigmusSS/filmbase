package ua.holovchenko.filmbase.converters;

import ua.holovchenko.filmbase.controllers.dto.FilmUploadDto;
import ua.holovchenko.filmbase.models.FilmModel;

public class UploadDtoConverter {
    public static FilmModel uploadedDtoToModel(FilmUploadDto dto) {
        FilmModel model = new FilmModel();
        model.setTitle(dto.getTitle());
        model.setYear(dto.getYear());
        model.setWrittenBy(dto.getWrittenBy());
        model.setProducedBy(dto.getProducedBy());
        model.setStarring(dto.getStarring());
        model.setRunningTime(dto.getRunningTime());
        model.setGenres(dto.getGenres());
        return model;
    }
}
