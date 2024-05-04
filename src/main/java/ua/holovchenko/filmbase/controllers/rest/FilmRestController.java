package ua.holovchenko.filmbase.controllers.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ua.holovchenko.filmbase.controllers.dto.FilmListResponse;
import ua.holovchenko.filmbase.controllers.dto.FilmUploadDto;
import ua.holovchenko.filmbase.controllers.dto.FilmsUploadResponse;
import ua.holovchenko.filmbase.controllers.dto.Filters;
import ua.holovchenko.filmbase.models.FilmModel;
import ua.holovchenko.filmbase.services.FilmService;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Rest controller for managing films.
 */
@RestController
@RequestMapping("api/films")
public class FilmRestController {
    private final FilmService service;

    /**
     * Constructor for FilmRestController.
     * @param service The FilmService instance to be injected.
     */
    @Autowired
    public FilmRestController(FilmService service) {
        this.service = service;
    }

    /**
     * Controller for POST api/films endpoint.
     * Create a new film.
     * @param model The FilmModel object representing the film to be created.
     * @return ResponseEntity with the created FilmModel object or a bad request response if validation fails.
     */
    @PostMapping
    public ResponseEntity<FilmModel> createFilm(@RequestBody FilmModel model) {
        if (!validateRequestBody(model)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.created(URI.create("api/films/" + model.getTitle())).body(service.createFilm(model));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Controller for GET api/films/{id} endpoint.
     * Get a film by ID.
     * @param id The ID of the film to retrieve.
     * @return ResponseEntity with the requested FilmModel object or a not found response if the film does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FilmModel> getFilm(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(service.getFilmById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Controller for PUT api/films/{id} endpoint.
     * Update an existing film.
     * @param filmId The ID of the film to update.
     * @param model The updated FilmModel object.
     * @return ResponseEntity with the updated FilmModel object or a bad request response if validation fails,
     * or not found if the film does not exist.
     */
    @PutMapping("/{filmId}")
    public ResponseEntity<FilmModel> updateFilm(@PathVariable Long filmId, @RequestBody FilmModel model) {
        if (!validateRequestBody(model)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok().body(service.updateFilm(filmId, model));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Controller for DELETE api/films/{id} endpoint.
     * Delete a film by ID.
     * @param id The ID of the film to delete.
     * @return ResponseEntity with a success message or a not found response if the film does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFilm(@PathVariable Long id) {
        try {
            service.deleteFilm(id);
            return ResponseEntity.ok("Deleted: " + id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Controller for POST api/films/_list endpoint.
     * List films based on filters.
     * @param filters The @link{ua.holovchenko.filmbase.controllers.dto.Filters} object
     *                containing filter parameters, page size and requested page number.
     * @return ResponseEntity with a @link{ua.holovchenko.filmbase.controllers.dto.FilmListResponse} containing the page of shortened films information.
     */
    @PostMapping("/_list")
    public ResponseEntity<FilmListResponse> listFilms(@RequestBody Filters filters) {
        return ResponseEntity.ok(service.listFilms(filters));
    }

    /**
     * Controller for POST api/films/_report endpoint
     * Download a film report based on filters.
     * @param filters The @link{ua.holovchenko.filmbase.controllers.dto.Filters} object containing filter parameters.
     * @return ResponseEntity with a StreamingResponseBody containing the report data,
     * accumulating in .csv file for download
     */
    @PostMapping(path = "/_report")
    public ResponseEntity<StreamingResponseBody> downloadFilmReport(@RequestBody Filters filters) {
        HttpHeaders headers = new HttpHeaders();
        StreamingResponseBody reportStream = service.createReport(filters);
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.setContentDispositionFormData("attachment", "filmReport_" + LocalDateTime.now() + ".csv");
        return ResponseEntity.ok().headers(headers).body(reportStream);
    }

    /**
     * Controller for POST api/films/upload endpoint.
     * Upload films from a JSON file.
     * @param json The MultipartFile containing the JSON file with film data.
     * @return ResponseEntity with a FilmsUploadResponse containing numbers of film data imported and failed to import.
     */
    @PostMapping(path = "/upload")
    public ResponseEntity<FilmsUploadResponse> uploadFilms(@RequestParam("file") MultipartFile json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TypeReference<List<FilmUploadDto>> listType = new TypeReference<>() {};
        try {
            List<FilmUploadDto> dtoList = mapper.readValue(json.getBytes(), listType);
            return ResponseEntity.ok(service.uploadJson(dtoList));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Validate a FilmModel object.
     * @param model The FilmModel object to be validated.
     * @return True if the film is valid, false otherwise.
     */
    private static boolean validateRequestBody(FilmModel model) {
        return !(model.getTitle() == null ||
                model.getTitle().isEmpty() ||
                model.getTitle().isBlank() ||
                model.getTitle().length() > 128 ||
                model.getDirectedBy() == null ||
                model.getDirectedBy().getName() == null ||
                model.getDirectedBy().getName().isEmpty() ||
                model.getDirectedBy().getName().isBlank() ||
                model.getDirectedBy().getName().length() > 128);
    }
}
