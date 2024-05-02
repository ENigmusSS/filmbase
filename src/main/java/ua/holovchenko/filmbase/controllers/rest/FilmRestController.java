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
import ua.holovchenko.filmbase.controllers.dto.FilmsUploadResponse;
import ua.holovchenko.filmbase.controllers.dto.Filters;
import ua.holovchenko.filmbase.models.FilmModel;
import ua.holovchenko.filmbase.services.FilmService;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController("api/films")
@RequestMapping("api/films")
public class FilmRestController {
    private final FilmService service;

    @Autowired
    public FilmRestController(FilmService service) {
        this.service = service;
    }

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


    @GetMapping("/{id}")
    public ResponseEntity<FilmModel> getFilm(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(service.getFilmById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFilm(@PathVariable Long id) {
        try {
            service.deleteFilm(id);
            return ResponseEntity.ok("Deleted: " + id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/_list")
    public ResponseEntity<FilmListResponse> listFilms(@RequestBody Filters filters) {
        return ResponseEntity.ok(service.listFilms(filters));
    }

    @PostMapping(path = "/_report")
    public ResponseEntity<StreamingResponseBody> downloadFilmReport(@RequestBody Filters filters) {
        HttpHeaders headers = new HttpHeaders();
        StreamingResponseBody reportStream = service.createReport(filters);
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.setContentDispositionFormData("attachment", "filmReport_" + LocalDateTime.now() + ".csv");
        return ResponseEntity.ok().headers(headers).body(reportStream);
    }

    @PostMapping(path = "/upload")
    public ResponseEntity<FilmsUploadResponse> uploadFilms(@RequestParam("file") MultipartFile json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        TypeReference<List<FilmModel>> listType = new TypeReference<>() {
        };
        try {
            List<FilmModel> modelList = mapper.readValue(json.getBytes(), listType);
            return ResponseEntity.ok(service.uploadJson(modelList));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

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
