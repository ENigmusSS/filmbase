package ua.holovchenko.filmbase.services;

import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ua.holovchenko.filmbase.controllers.dto.*;
import ua.holovchenko.filmbase.converters.FilmModelEntityConverter;
import ua.holovchenko.filmbase.entities.Director;
import ua.holovchenko.filmbase.entities.Film;
import ua.holovchenko.filmbase.models.FilmModel;
import ua.holovchenko.filmbase.repositories.DirectorRepository;
import ua.holovchenko.filmbase.repositories.FilmRepository;
import ua.holovchenko.filmbase.repositories.criteria.FilmSpecification;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

import static ua.holovchenko.filmbase.converters.FilmModelEntityConverter.*;
        import static ua.holovchenko.filmbase.converters.UploadDtoConverter.uploadedDtoToModel;

/**
 * Service class for Film entities.
 */
@Service
public class FilmService {
    private final FilmRepository repo;
    private final FilmSpecification spec;
    private final DirectorRepository directorRepository;

    /**
     * Constructor for FilmService.
     * @param repo The repository for Film entities.
     * @param spec The specification for Film entities.
     * @param directorRepository The repository for Director entities.
     */
    @Autowired
    public FilmService(FilmRepository repo, FilmSpecification spec, DirectorRepository directorRepository) {
        this.repo = repo;
        this.spec = spec;
        this.directorRepository = directorRepository;
    }

    /**
     * Creates a new film.
     * @param model The FilmModel representing the film to be created.
     * @return The created FilmModel.
     */
    public FilmModel createFilm(FilmModel model) {
        Film film = filmModelToEntity(model);
        return filmEntityToModel(repo.save(film));
    }

    /**
     * Retrieves a film by its id.
     * @param id The id of the film.
     * @return The FilmModel representing the retrieved film.
     * @throws NoSuchElementException if the film with the specified id is not found.
     */
    public FilmModel getFilmById(Long id) {
        return filmEntityToModel(repo.findById(id).orElseThrow());
    }

    /**
     * Updates an existing film.
     * @param id The id of the film to be updated.
     * @param model The FilmModel representing the updated film.
     * @return The updated FilmModel.
     * @throws NoSuchElementException if the film with the specified id is not found.
     * @throws ValidationException if the film with the updated title already exists.
     */
    public FilmModel updateFilm(Long id, FilmModel model) {
        Film film = filmModelToEntity(model);
        if (!repo.existsById(id)) throw new NoSuchElementException("Film not found");
        if (repo.existsByTitle(film.getTitle())) throw new ValidationException("Film already exists");
        return filmEntityToModel(repo.saveAndFlush(film));
    }

    /**
     * Deletes a film.
     * @param id The id of the film to be deleted.
     * @throws NoSuchElementException if the film with the specified id is not found.
     */
    public void deleteFilm(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new NoSuchElementException("Film not found" + id);
        }
    }

    /**
     * Retrieves a list of films based on specified filters.
     * @param filters The Filters object containing filter criteria.
     * @return The FilmListResponse containing the list of films and pagination information.
     */
    public FilmListResponse listFilms(Filters filters) {
        Pageable pageable = PageRequest.of(filters.getPage() - 1, filters.getPageSize());
        FilmListResponse response = new FilmListResponse();
        Page<Film> page = repo.findAll(spec.filmSpecification(filters), pageable);
        response.setTotalPages(page.getTotalPages());
        response.setFilms(
                page.get()
                        .map(FilmModelEntityConverter::filmEntityToModel)
                        .map(FilmListDto::new)
                        .toList()
        );
        return response;
    }

    /**
     * Generates a CSV report of films based on specified filters.
     * @param filters The Filters object containing filter criteria.
     * @return The StreamingResponseBody representing the CSV report.
     */
    public StreamingResponseBody createReport(Filters filters) {
        return OutputStream -> {
            try (Writer writer = new OutputStreamWriter(OutputStream, StandardCharsets.UTF_8)) {
                try {
                    new StatefulBeanToCsvBuilder<FilmListDto>(writer).build()
                            .write(
                                    repo.findAll(spec.filmSpecification(filters)).stream()
                                            .map(FilmModelEntityConverter::filmEntityToModel)
                                            .map(FilmListDto::new)
                            );
                } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
                    throw new RuntimeException("This was not possible, but");
                }
            }
        };
    }

    /**
     * Uploads a list of films from JSON.
     * @param dtoList The list of FilmUploadDto objects containing film data.
     * @return The FilmsUploadResponse containing import statistics.
     */
    public FilmsUploadResponse uploadJson(List<FilmUploadDto> dtoList) {
        FilmsUploadResponse response = new FilmsUploadResponse();
        for (FilmUploadDto dto: dtoList) {
            try {
                if (repo.existsByTitle(dto.getTitle())) throw  new ValidationException();
                Director director = directorRepository.findByName(dto.getDirectedBy());
                if (director == null) throw new ValidationException();
                FilmModel model = uploadedDtoToModel(dto);
                repo.save(filmModelToEntity(model, director));
                response.setImported(response.getImported() + 1);
            } catch (Exception e) {
                response.setFailed(response.getFailed() + 1);
            }
        }
        return response;
    }
}

