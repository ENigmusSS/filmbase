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
import ua.holovchenko.filmbase.controllers.dto.FilmListDto;
import ua.holovchenko.filmbase.controllers.dto.FilmListResponse;
import ua.holovchenko.filmbase.controllers.dto.FilmsUploadResponse;
import ua.holovchenko.filmbase.controllers.dto.Filters;
import ua.holovchenko.filmbase.converters.FilmModelEntityConverter;
import ua.holovchenko.filmbase.entities.Film;
import ua.holovchenko.filmbase.models.FilmModel;
import ua.holovchenko.filmbase.repositories.FilmRepository;
import ua.holovchenko.filmbase.repositories.criteria.FilmSpecification;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

import static ua.holovchenko.filmbase.converters.FilmModelEntityConverter.filmEntityToModel;
import static ua.holovchenko.filmbase.converters.FilmModelEntityConverter.filmModelToEntity;

@Service
public class FilmService {
    private final FilmRepository repo;
    private final FilmSpecification spec;

    @Autowired
    public FilmService(FilmRepository repo, FilmSpecification spec) {
        this.repo = repo;
        this.spec = spec;
    }

    public FilmModel createFilm(FilmModel model) {
        Film film;
        film = filmModelToEntity(model);
        return filmEntityToModel(repo.save(film));
    }

    public FilmModel getFilmById(Long id) {
        return filmEntityToModel(repo.findById(id).orElseThrow());
    }

    public FilmModel updateFilm(Long id, FilmModel model) {
        Film film;
        film = filmModelToEntity(model);
        if (!repo.existsById(id)) throw new NoSuchElementException("Film not found");
        if (repo.existsByTitle(film.getTitle())) throw new ValidationException("Film already exists");
        return filmEntityToModel(repo.saveAndFlush(film));

    }

    public void deleteFilm(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new NoSuchElementException("Film not found" + id);
        }
    }

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
                    throw new RuntimeException("This was not possible, but...");
                }
            }
        };
    }

    public FilmsUploadResponse uploadJson(List<FilmModel> modelList) {
        FilmsUploadResponse response = new FilmsUploadResponse();
        for (FilmModel model:
             modelList) {
            try {
                repo.save(filmModelToEntity(model));
                response.setImported(response.getImported() + 1);
            } catch (Exception e) {
                response.setFailed(response.getFailed() + 1);
            }
        }
        return response;
    }
}
