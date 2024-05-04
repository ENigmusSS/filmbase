package ua.holovchenko.filmbase.models;

import lombok.*;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Model for {@link ua.holovchenko.filmbase.entities.Director}
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DirectorModel implements Serializable {
    private long id;
    private String name;
    private Set<FilmModel> films = new LinkedHashSet<>();

    public DirectorModel(String name) {
        this.name = name;
    }

    public DirectorModel(long id, String name) {
        this.id = id;
        this.name = name;
    }
}