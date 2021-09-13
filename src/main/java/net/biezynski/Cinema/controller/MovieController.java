package net.biezynski.Cinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import net.biezynski.Cinema.exception.NotFoundFilmException;
import net.biezynski.Cinema.model.MovieDto;
import net.biezynski.Cinema.request.AddMovieRequest;
import net.biezynski.Cinema.request.CopyMovieRequest;
import net.biezynski.Cinema.request.EditMovieRequest;
import net.biezynski.Cinema.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/movie")
public class MovieController {

    private final MovieService movieService;

    @Operation(summary = "Add new film", description = "Add new film, in body pass name of movie, description, date, length and price of tickets")
    @PostMapping("/add")
    public MovieDto addMovie(@RequestBody @Valid AddMovieRequest addMovieRequest) {
        return movieService.addMovie(addMovieRequest);
    }

    @Operation(summary = "Get info about one movie", description = "Get full info about one movie")
    @GetMapping("/{id}")
    public MovieDto getOneMovie(@PathVariable Long id){
        try {
            return movieService.getOneMovie(id);
        }catch (NotFoundFilmException notFoundFilmException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundFilmException.getMessage());
        }
    }

    @Operation(summary = "Edit info about one movie", description = "Edit info about movie pass movie id and name of movie/description/date/length")
    @PatchMapping("/{id}")
    public MovieDto editOneMovie(@RequestBody EditMovieRequest editMovieRequest, @PathVariable Long id) {
        try{
            return movieService.editOneMovie(editMovieRequest, id);
        }catch (NotFoundFilmException notFoundFilmException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundFilmException.getMessage());
        }
    }

    @Operation(summary = "Copy movie with new planned Show Date", description = "Copy movie with new planned Show Date, pass movie id and new date of movie")
    @PostMapping("/{id}")
    public MovieDto copyMovie(@RequestBody CopyMovieRequest copyMovieRequest, @PathVariable Long id) {
        try{
            return movieService.copyMovie(copyMovieRequest, id);
        }catch (NotFoundFilmException notFoundFilmException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundFilmException.getMessage());
        }
    }

    @Operation(summary = "List all movies that have 15 or less Occupied Seats", description = "List all movies that have 15 or less Occupied Seats")
    @GetMapping("/seats")
    public List<MovieDto> getMoviesWithLittleSeats(){
        return movieService.checkFilmsWithLessSeats();
    }
}
