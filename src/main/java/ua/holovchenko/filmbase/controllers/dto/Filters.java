package ua.holovchenko.filmbase.controllers.dto;



import lombok.*;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Filters {
    int page = 1;
    int pageSize = 10;
    Integer year;
    Integer yearSince;
    Integer yearTo;
    String directedBy;
    Set<String> writtenBy;
    Set<String> producedBy;
    Set<String> starring;
    Integer runningTime;
    Integer runningTimeMin;
    Integer runningTimeMax;
    Set<String> genres;
}
