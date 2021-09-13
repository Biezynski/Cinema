package net.biezynski.Cinema.service;

import lombok.RequiredArgsConstructor;
import net.biezynski.Cinema.Utils.Utils;
import net.biezynski.Cinema.exception.NotFoundFilmException;
import net.biezynski.Cinema.mappers.MovieEntityMapper;
import net.biezynski.Cinema.mappers.MovieMapper;
import net.biezynski.Cinema.model.*;
import net.biezynski.Cinema.repository.MovieRepository;
import net.biezynski.Cinema.request.AddMovieRequest;
import net.biezynski.Cinema.request.CopyMovieRequest;
import net.biezynski.Cinema.request.EditMovieRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;
    private final MovieEntityMapper movieEntityMapper;

    @Override
    public MovieDto addMovie(AddMovieRequest addMovieRequest) {
        List<Ticket> tickets = new ArrayList<>();
        Movie movie = Movie.builder()
                .movieName(addMovieRequest.getMovieName())
                .dateString(addMovieRequest.getDateString())
                .description(addMovieRequest.getDescription())
                .movieLength(addMovieRequest.getMovieLength())
                .build();
        Utils.generateSeatsIds().forEach(
                seatId -> {
                    Ticket newTicket = Ticket.builder()
                            .ticketNumber(UUID.randomUUID().toString())
                            .seatNumber(seatId)
                            .ticketPrice(addMovieRequest.getPriceOfTicket())
                            .movie(movie)
                            .build();
                    tickets.add(newTicket);
                }
        );
        movie.setTickets(tickets);
        movieRepository.save(movie);
        return movieMapper.mapMovieToMovieDto(movie);
    }

    @Override
    public MovieDto getOneMovie(Long id) throws NotFoundFilmException {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new NotFoundFilmException(id));
        return movieMapper.mapMovieToMovieDto(movie);
    }

    @Override
    public MovieDto editOneMovie(EditMovieRequest request, Long id) throws NotFoundFilmException {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new NotFoundFilmException(id));
        movieEntityMapper.updateMovieEntity(request, movie);
        movieRepository.save(movie);
        return movieMapper.mapMovieToMovieDto(movie);
    }

    @Override
    public MovieDto copyMovie(CopyMovieRequest copyMovieRequest, Long id) throws NotFoundFilmException {
        Movie movieFromRepository = movieRepository.findById(id).orElseThrow(() -> new NotFoundFilmException(id));
        List<Ticket> tickets = new ArrayList<>();
        Movie movie = Movie.builder()
                .movieLength(movieFromRepository.getMovieLength())
                .movieName(movieFromRepository.getMovieName())
                .dateString(copyMovieRequest.getDateString())
                .description(movieFromRepository.getDescription())
                .build();
        Utils.generateSeatsIds().forEach(
                seatId -> {
                    Ticket newTicket = Ticket.builder()
                            .ticketNumber(UUID.randomUUID().toString())
                            .seatNumber(seatId)
                            .ticketPrice(movieFromRepository.getTickets().stream().max(Comparator.comparing(Ticket::getTicketPrice)).get().getTicketPrice())
                            .movie(movie)
                            .build();
                    tickets.add(newTicket);
                });
        movie.setTickets(tickets);
        movieRepository.save(movie);
        return movieMapper.mapMovieToMovieDto(movie);
    }

    @Override
    public List<MovieDto> checkFilmsWithLessSeats() {
        List<Movie> moviesFromRepo = movieRepository.findAll();
        List<Movie> filteredMovie = moviesFromRepo.stream().filter(movie -> movie.getTickets().size() < 15).collect(Collectors.toList());
        return movieMapper.mapMovieListToMovieListDto(filteredMovie);
    }

}
