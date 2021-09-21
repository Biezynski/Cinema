package net.biezynski.Cinema.service;

import lombok.RequiredArgsConstructor;
import net.biezynski.Cinema.exception.NotFoundFilmException;
import net.biezynski.Cinema.exception.OccupiedOrNotExistSeat;
import net.biezynski.Cinema.mappers.TicketMapper;
import net.biezynski.Cinema.model.*;
import net.biezynski.Cinema.repository.MovieRepository;
import net.biezynski.Cinema.repository.SoldTicketRepository;
import net.biezynski.Cinema.repository.TicketRepository;
import net.biezynski.Cinema.request.BookTicketRequest;
import net.biezynski.Cinema.request.BookTicketWithDiscountRequest;
import net.biezynski.Cinema.request.BookTicketsWithSameRow;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final MovieRepository movieRepository;
    private final TicketMapper ticketMapper;
    private final SoldTicketRepository soldTicketRepository;

    @Override
    public TicketDto bookOneTicket(BookTicketRequest bookTicketRequest) throws NotFoundFilmException, OccupiedOrNotExistSeat {
        Movie movieFromRepository = getMovieFromRepository(bookTicketRequest.getMovieId());
        Ticket ticket = getTicket(bookTicketRequest, movieFromRepository);
        movieFromRepository.getTickets().remove(ticket);
        movieRepository.save(movieFromRepository);
        markTicketAsSoledAndAddToArchive(ticket);
        return ticketMapper.mapTicketToTicketDto(ticket);
    }

    private void markTicketAsSoledAndAddToArchive(Ticket ticket) {
        SoldTicket soldTicket = ticketMapper.mapTicketToSoldTicket(ticket);
        soldTicketRepository.save(soldTicket);
    }

    private Ticket getTicket(BookTicketRequest bookTicketRequest, Movie movieFromRepository) throws OccupiedOrNotExistSeat {
        return movieFromRepository
                .getTickets().stream().filter((ticket -> bookTicketRequest.getSeatId().equals(ticket.getSeatNumber()))).findFirst()
                .orElseThrow(() -> new OccupiedOrNotExistSeat(bookTicketRequest.getMovieId()));
    }

    @Override
    public TicketDto bookOneTicketWithDiscount(BookTicketWithDiscountRequest bookTicketWithDiscountRequest) throws NotFoundFilmException, OccupiedOrNotExistSeat {
        Movie movieFromRepository = getMovieFromRepository(bookTicketWithDiscountRequest.getMovieId());
        Ticket ticket = getTicket(bookTicketWithDiscountRequest, movieFromRepository);
        movieFromRepository.getTickets().remove(ticket);
        BigDecimal ticketPriceBeforeDiscount = ticket.getTicketPrice();
        ticket.setTicketPrice(calculatePercentage(bookTicketWithDiscountRequest.getDiscountInPercentage(), ticketPriceBeforeDiscount));
        movieRepository.save(movieFromRepository);
        SoldTicket soldTicket = ticketMapper.mapTicketToSoldTicket(ticket);
        soldTicket.setDiscount(bookTicketWithDiscountRequest.getDiscountInPercentage());
        soldTicketRepository.save(soldTicket);
        return ticketMapper.mapTicketToTicketDto(ticket);
    }

    private Ticket getTicket(BookTicketWithDiscountRequest bookTicketWithDiscountRequest, Movie movieFromRepository) throws OccupiedOrNotExistSeat {
        return movieFromRepository.getTickets().stream().filter((ticket -> bookTicketWithDiscountRequest.getSeatId().equals(ticket.getSeatNumber()))).findFirst()
                .orElseThrow(() -> new OccupiedOrNotExistSeat(bookTicketWithDiscountRequest.getMovieId()));
    }

    @Override
    public List<TicketDto> getAllTicketsForMovie(Long movieId) throws NotFoundFilmException {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new NotFoundFilmException(movieId));
        return ticketMapper.mapListTicketsToTicketsDto(movie.getTickets());
    }

    @Override
    public List<TicketDto> bookTicketsWithSameRow(BookTicketsWithSameRow bookTicketsWithSameRow) throws NotFoundFilmException {
        Movie movieFromRepository = getMovieFromRepository(bookTicketsWithSameRow.getMovieId());
        List<Ticket> temp = new ArrayList<Ticket>();
        for (int i = 0; i <  movieFromRepository.getTickets().size(); i++) {
            for (int j = i; j <= movieFromRepository.getTickets().size(); j++) {
                CharSequence charSequence = String.valueOf(j);
                if (movieFromRepository.getTickets().stream().anyMatch(ticket -> ticket.getSeatNumber().equals(bookTicketsWithSameRow.getRowLetter() + charSequence))) {
                    temp.add(movieFromRepository.getTickets().stream().filter(ticket -> ticket.getSeatNumber().equals(bookTicketsWithSameRow.getRowLetter() + charSequence)).findFirst().get());
                    if (temp.size() == bookTicketsWithSameRow.getNumberOfSeats()) {
                        return ticketMapper.mapListTicketsToTicketsDto(temp);
                    }
                } else {
                    temp.clear();
                    break;
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<SoldTicketDto> getAllSoldTicketWithDiscount() {
        List<SoldTicket> soldTicketsFromRepo= soldTicketRepository.findAllOrderByDiscount().stream()
                .filter(soldTicket -> soldTicket.getDiscount()>0).collect(Collectors.toList());
        return ticketMapper.mapListSoldTicketToListSoldTicketDto(soldTicketsFromRepo);
    }

    private Movie getMovieFromRepository(Long movieId) throws NotFoundFilmException {
        return movieRepository.findById(movieId).orElseThrow(() -> new NotFoundFilmException(movieId));
    }

    private BigDecimal calculatePercentage(int discountInPercentage, BigDecimal currentPrice) {
        BigDecimal finalDiscount =  new BigDecimal(discountInPercentage).divide(new BigDecimal(100), 2, RoundingMode.CEILING).multiply(currentPrice);
        return currentPrice.subtract(finalDiscount);
    }
}

