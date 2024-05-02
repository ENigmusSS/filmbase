package ua.holovchenko.filmbase.controllers.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FilmListResponse {
    List<FilmListDto> films;
    int totalPages;
}
