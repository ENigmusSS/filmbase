package ua.holovchenko.filmbase.converters;

import ua.holovchenko.filmbase.controllers.dto.FilmUploadDto;
import ua.holovchenko.filmbase.models.FilmModel;

/**
 * Converter class to convert between FilmUploadDto and FilmModel.
 */
public class UploadDtoConverter {

    /**
     * Converts a FilmUploadDto to a FilmModel.
     * @param dto The FilmUploadDto to be converted.
     * @return The converted FilmModel. !director information isn't set at this point!
     */
    public static FilmModel uploadedDtoToModel(FilmUploadDto dto) {
        // Create a new FilmModel object
        FilmModel model = new FilmModel();

        // Set properties from the FilmUploadDto to the FilmModel
        model.setTitle(dto.getTitle());
        model.setYear(dto.getYear());
        model.setWrittenBy(dto.getWrittenBy());
        model.setProducedBy(dto.getProducedBy());
        model.setStarring(dto.getStarring());
        model.setRunningTime(dto.getRunningTime());
        model.setGenres(dto.getGenres());

        return model; // Return the converted FilmModel
    }
}
