package ua.holovchenko.filmbase.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a Director entity.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "directors")
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Film> films = new LinkedHashSet<>();

    /**
     * Constructor for a Director with only a name.
     * @param name The name of the director.
     */
    public Director(String name) {
        this.name = name;
    }

    /**
     * Constructor for a Director with a name and a set of films.
     * @param name The name of the director.
     * @param films The films directed by this director.
     */
    public Director(String name, Set<Film> films) {
        this.name = name;
        this.films = films;
    }

    /**
     * Constructor for a Director with an id and a name.
     * @param id The id of the director.
     * @param name The name of the director.
     */
    public Director(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Custom equals method to compare Directors.
     * @param o The object to compare.
     * @return True if the Directors are equal, false otherwise.
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Director director = (Director) o;
        return getName() != null && Objects.equals(getName(), director.getName());
    }

    /**
     * Custom hashCode method.
     * @return The hash code.
     */
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
