package ua.holovchenko.filmbase.entities;

import jakarta.persistence.*;
import lombok.*;

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