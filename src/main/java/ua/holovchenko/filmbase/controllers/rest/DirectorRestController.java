package ua.holovchenko.filmbase.controllers.rest;

import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.holovchenko.filmbase.models.DirectorModel;
import ua.holovchenko.filmbase.services.DirectorService;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Rest controller for managing directors.
 */
@RestController
@RequestMapping("api/directors")
public class DirectorRestController {
    private final DirectorService service;

    /**
     * Constructor for DirectorRestController.
     * @param service The DirectorService instance to be injected.
     */
    @Autowired
    public DirectorRestController(DirectorService service) {
        this.service = service;
    }

    /**
     * Controller for GET api/directors endpoint.
     * Get all directors.
     * @return ResponseEntity with a list of @link{ua.holovchenko.filmbase.models.DirectorModel}.
     */
    @GetMapping
    public ResponseEntity<List<DirectorModel>> getDirectors() {
        return ResponseEntity.ok().body(service.findAll());
    }

    /**
     * Controller for POST api/directors endpoint.
     * Add a new director.
     * @param model @link{ua.holovchenko.filmbase.models.DirectorModel} object to be added.
     * @return ResponseEntity with the added DirectorModel object or a bad request response if validation fails.
     */
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

    /**
     * Controller for PUT api/directors/{directorId} endpoint.
     * Update an existing director.
     * @param directorId The ID of the director to be updated.
     * @param model The updated DirectorModel object.
     * @return ResponseEntity with the updated DirectorModel object or a bad request response if validation fails.
     */
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

    /**
     * Controller for DELETE api/directors/{id} endpoint.
     * Delete a director by ID.
     * @param id The ID of the director to be deleted.
     * @return ResponseEntity with a success message or a not found response if the director does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDirector(@PathVariable Long id) {
        try {
            service.deleteDirector(id);
            return ResponseEntity.ok().body("Director deleted successfully: " + id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Validate a DirectorModel object.
     * @param model The DirectorModel object to be validated.
     * @return True if the director is valid, false otherwise.
     */
    private boolean validateDirector(DirectorModel model) {
        return !(model.getName() == null ||
                model.getName().isEmpty() ||
                model.getName().isBlank() ||
                model.getName().length() > 128);
    }
}
