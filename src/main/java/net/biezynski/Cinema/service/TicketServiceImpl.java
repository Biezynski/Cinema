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
        Movie movieFromRepository = movieRepository.findById(bookTicketRequest.getMovieId()).orElseThrow(() -> new NotFoundFilmException(bookTicketRequest.getMovieId()));
        Ticket ticketFromFilter = movieFromRepository
                .getTickets().stream().filter((ticket -> bookTicketRequest.getSeatId().equals(ticket.getSeatNumber()))).findFirst()
                .orElseThrow(() -> new OccupiedOrNotExistSeat(bookTicketRequest.getMovieId()));
        movieFromRepository.getTickets().remove(ticketFromFilter);
        movieRepository.save(movieFromRepository);

        SoldTicket soldTicket = ticketMapper.mapTicketToSoldTicket(ticketFromFilter);
        soldTicketRepository.save(soldTicket);
        return ticketMapper.mapTicketToTicketDto(ticketFromFilter);
    }

    @Override
    public TicketDto bookOneTicketWithDiscount(BookTicketWithDiscountRequest bookTicketWithDiscountRequest) throws NotFoundFilmException, OccupiedOrNotExistSeat {
        Movie movieFromRepository = movieRepository.findById(bookTicketWithDiscountRequest.getMovieId()).orElseThrow(() -> new NotFoundFilmException(bookTicketWithDiscountRequest.getMovieId()));
        Ticket ticketFromFilter = movieFromRepository.getTickets().stream().filter((ticket -> bookTicketWithDiscountRequest.getSeatId().equals(ticket.getSeatNumber()))).findFirst()
                .orElseThrow(() -> new OccupiedOrNotExistSeat(bookTicketWithDiscountRequest.getMovieId()));
        movieFromRepository.getTickets().remove(ticketFromFilter);
        BigDecimal ticketPriceBeforeDiscount = ticketFromFilter.getTicketPrice();
        ticketFromFilter.setTicketPrice(calculatePercentage(bookTicketWithDiscountRequest.getDiscountInPercentage(), ticketPriceBeforeDiscount));
        movieRepository.save(movieFromRepository);
        SoldTicket soldTicket = ticketMapper.mapTicketToSoldTicket(ticketFromFilter);
        soldTicket.setDiscount(bookTicketWithDiscountRequest.getDiscountInPercentage());
        soldTicketRepository.save(soldTicket);
        return ticketMapper.mapTicketToTicketDto(ticketFromFilter);
    }

    @Override
    public List<TicketDto> getAllTicketsForMovie(Long movieId) throws NotFoundFilmException {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new NotFoundFilmException(movieId));
        return ticketMapper.mapListTicketsToTicketsDto(movie.getTickets());
    }

    @Override
    public List<TicketDto> bookTicketsWithSameRow(BookTicketsWithSameRow bookTicketsWithSameRow) throws NotFoundFilmException {
        Movie movieFromRepository = movieRepository.findById(bookTicketsWithSameRow.getMovieId()).orElseThrow(() -> new NotFoundFilmException(bookTicketsWithSameRow.getMovieId()));
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

    private BigDecimal calculatePercentage(int discountInPercentage, BigDecimal currentPrice) {
        BigDecimal discountInPerc = new BigDecimal(discountInPercentage);
        BigDecimal oneHunred = new BigDecimal(100);
        BigDecimal division = discountInPerc.divide(oneHunred, 2, RoundingMode.CEILING);
        BigDecimal finalDiscount = division.multiply(currentPrice);
        return currentPrice.subtract(finalDiscount);
    }
}

