package ua.holovchenko.filmbase.controllers.dto;

import lombok.Value;
import ua.holovchenko.filmbase.models.DirectorModel;
import ua.holovchenko.filmbase.models.FilmModel;

@Value
public class FilmListDto {
    long id;
    String title;
    Integer year;
    DirectorModel directedBy;
    Integer runningTime;

    public FilmListDto(FilmModel film) {
        this.id = film.getId();
        this.title = film.getTitle();
        this.year = film.getYear();
        this.directedBy = film.getDirectedBy();
        this.runningTime = film.getRunningTime();
    }
}
