package net.biezynski.Cinema.service;

import net.biezynski.Cinema.exception.NotFoundFilmException;
import net.biezynski.Cinema.model.MovieDto;
import net.biezynski.Cinema.request.AddMovieRequest;
import net.biezynski.Cinema.request.CopyMovieRequest;
import net.biezynski.Cinema.request.EditMovieRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface MovieService {
    MovieDto addMovie(AddMovieRequest addMovieRequest);

    MovieDto getOneMovie(Long id) throws NotFoundFilmException;

    MovieDto editOneMovie(EditMovieRequest request, Long id) throws NotFoundFilmException;

    MovieDto copyMovie(CopyMovieRequest copyMovieRequest, Long id) throws NotFoundFilmException;

    List<MovieDto> checkFilmsWithLessSeats();
}
