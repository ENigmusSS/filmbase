package ua.holovchenko.filmbase.controllers.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class FilmsUploadResponse {
    int imported = 0;
    int failed = 0;
}
