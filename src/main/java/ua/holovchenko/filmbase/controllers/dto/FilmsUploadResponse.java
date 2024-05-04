package ua.holovchenko.filmbase.controllers.dto;

import lombok.*;

/**
 * Response body for upload request.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class FilmsUploadResponse {
    int imported = 0;
    int failed = 0;
}
