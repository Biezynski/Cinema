package net.biezynski.Cinema.service;

import net.biezynski.Cinema.exception.NotFoundFilmException;
import net.biezynski.Cinema.exception.OccupiedOrNotExistSeat;
import net.biezynski.Cinema.model.SoldTicketDto;
import net.biezynski.Cinema.model.TicketDto;
import net.biezynski.Cinema.request.BookTicketRequest;
import net.biezynski.Cinema.request.BookTicketWithDiscountRequest;
import net.biezynski.Cinema.request.BookTicketsWithSameRow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TicketService {
    TicketDto bookOneTicket(BookTicketRequest bookTicketRequest) throws NotFoundFilmException, OccupiedOrNotExistSeat;

    TicketDto bookOneTicketWithDiscount(BookTicketWithDiscountRequest bookTicketWithDiscountRequest) throws NotFoundFilmException, OccupiedOrNotExistSeat;

    List<TicketDto> getAllTicketsForMovie(Long id) throws NotFoundFilmException;

    List<TicketDto> bookTicketsWithSameRow(BookTicketsWithSameRow bookTicketsWithSameRow) throws NotFoundFilmException;

    List<SoldTicketDto> getAllSoldTicketWithDiscount();
}
