package ua.holovchenko.filmbase.controllers.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Response body DTO containing a list of shortened Film DTOs and number of such pages base holds.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FilmListResponse {
    List<FilmListDto> films;
    int totalPages;
}
