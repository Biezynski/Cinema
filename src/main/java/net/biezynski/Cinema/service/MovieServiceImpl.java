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
        Movie movie = getMovie(addMovieRequest);
        movieRepository.save(movie);
        return movieMapper.mapMovieToMovieDto(movie);
    }

    @Override
    public MovieDto copyMovie(CopyMovieRequest copyMovieRequest, Long id) throws NotFoundFilmException {
        Movie movieFromRepository = getMovieFromRepository(id);
        Movie movie = getMovie(copyMovieRequest, movieFromRepository);
        movieRepository.save(movie);
        return movieMapper.mapMovieToMovieDto(movie);
    }

    @Override
    public MovieDto getOneMovie(Long id) throws NotFoundFilmException {
        Movie movie = getMovieFromRepository(id);
        return movieMapper.mapMovieToMovieDto(movie);
    }

    @Override
    public MovieDto editOneMovie(EditMovieRequest request, Long id) throws NotFoundFilmException {
        Movie movie = getMovieFromRepository(id);
        movieEntityMapper.updateMovieEntity(request, movie);
        movieRepository.save(movie);
        return movieMapper.mapMovieToMovieDto(movie);
    }

    @Override
    public List<MovieDto> checkFilmsWithLessSeats() {
        List<Movie> moviesFromRepo = movieRepository.findAll();
        List<Movie> filteredMovie = moviesFromRepo.stream().filter(movie -> movie.getTickets().size() < 15).collect(Collectors.toList());
        return movieMapper.mapMovieListToMovieListDto(filteredMovie);
    }

    private Movie getMovieFromRepository(Long id) throws NotFoundFilmException {
        return movieRepository.findById(id).orElseThrow(()-> new NotFoundFilmException(id));
    }

    private Movie getMovie(CopyMovieRequest copyMovieRequest, Movie movieFromRepository) {
        List<Ticket> tickets = new ArrayList<>();
        Movie movie = createMovieByBuilder(copyMovieRequest, movieFromRepository);
        createMovieTickets(movieFromRepository, tickets, movie);
        movie.setTickets(tickets);
        return movie;
    }

    private Movie createMovieByBuilder(CopyMovieRequest copyMovieRequest, Movie movieFromRepository) {
        return Movie.builder()
                .movieLength(movieFromRepository.getMovieLength())
                .movieName(movieFromRepository.getMovieName())
                .dateString(copyMovieRequest.getDateString())
                .description(movieFromRepository.getDescription())
                .build();
    }

    private void createMovieTickets(Movie movieFromRepository, List<Ticket> tickets, Movie movie) {
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
    }

    private Movie getMovie(AddMovieRequest addMovieRequest) {
        List<Ticket> tickets = new ArrayList<>();
        Movie movie = createMovieByBuilder(addMovieRequest);
        createMovieTickets(addMovieRequest, tickets, movie);
        movie.setTickets(tickets);

        return movie;
    }

    private Movie createMovieByBuilder(AddMovieRequest addMovieRequest) {
        return Movie.builder()
                .movieName(addMovieRequest.getMovieName())
                .dateString(addMovieRequest.getDateString())
                .description(addMovieRequest.getDescription())
                .movieLength(addMovieRequest.getMovieLength())
                .build();
    }

    private void createMovieTickets(AddMovieRequest addMovieRequest, List<Ticket> tickets, Movie movie) {
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
    }
}
