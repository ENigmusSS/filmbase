package ua.holovchenko.filmbase.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

/**
 * Model for {@link ua.holovchenko.filmbase.entities.Film}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilmModel implements Serializable {
    long id;
    String title;
    Integer year;
    @JsonProperty("directed by")
    DirectorModel directedBy;
    @JsonProperty("written by")
    Set<String> writtenBy;
    @JsonProperty("produced by")
    Set<String> producedBy;
    Set<String> starring;
    @JsonProperty("running time")
    Integer runningTime;
    Set<String> genres;
}