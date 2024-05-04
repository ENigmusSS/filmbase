package ua.holovchenko.filmbase.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a Film entity.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "year")
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "directed_by")
    private Director directedBy;

    @Column(name = "written_by", length = Integer.MAX_VALUE)
    private String writtenBy;

    @Column(name = "produced_by", length = Integer.MAX_VALUE)
    private String producedBy;

    @Column(name = "starring", length = Integer.MAX_VALUE)
    private String starring;

    @Column(name = "running_time")
    private Integer runningTime;

    @Column(name = "genres", length = Integer.MAX_VALUE)
    private String genres;

    /**
     * Constructor for a Film with all attributes.
     * @param title The title of the film.
     * @param year The year the film was released.
     * @param directedBy The Director who directed the film.
     * @param writtenBy The writers of the film.
     * @param producedBy The producers of the film.
     * @param starring The main actors of the film.
     * @param runningTime The duration of the film in minutes.
     * @param genres The genres of the film.
     */
    public Film(String title, Integer year, Director directedBy, String writtenBy, String producedBy, String starring, Integer runningTime, String genres) {
        this.title = title;
        this.year = year;
        this.directedBy = directedBy;
        this.writtenBy = writtenBy;
        this.producedBy = producedBy;
        this.starring = starring;
        this.runningTime = runningTime;
        this.genres = genres;
    }
}
