package ua.holovchenko.filmbase.controllers.rest;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.holovchenko.filmbase.models.DirectorModel;
import ua.holovchenko.filmbase.services.DirectorService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController("api/directors")
@RequestMapping("api/directors")
public class DirectorRestController {
    private final DirectorService service;

    @Autowired
    public DirectorRestController(DirectorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DirectorModel>> getDirectors() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @PostMapping
    public ResponseEntity<DirectorModel> addDirector(@RequestBody DirectorModel model) {
        if (!validateDirector(model)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok().body(service.createDirector(model));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{directorId}")
    public ResponseEntity<DirectorModel> updateDirector(@PathVariable Long directorId, @RequestBody DirectorModel model) {
        if (!validateDirector(model)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok().body(service.updateDirector(directorId, model));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDirector(@PathVariable Long id) {
        try {
            service.deleteDirector(id);
            return ResponseEntity.ok().body("Director deleted successfully: " + id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean validateDirector(DirectorModel model) {
        return !(model.getName() == null ||
                model.getName().isEmpty() ||
                model.getName().isBlank() ||
                model.getName().length() > 128);
    }
}
