package ua.holovchenko.filmbase.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * DTO for data uploaded via .json file for conversion through FilmModel to Film entity.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilmUploadDto {
    String title;
    Integer year;
    @JsonProperty("directed by")
    String directedBy;
    @JsonProperty("written by")
    Set<String> writtenBy;
    @JsonProperty("produced by")
    Set<String> producedBy;
    Set<String> starring;
    @JsonProperty("running time")
    Integer runningTime;
    Set<String> genres;
}
