package net.biezynski.Cinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import net.biezynski.Cinema.exception.NotFoundFilmException;
import net.biezynski.Cinema.exception.OccupiedOrNotExistSeat;
import net.biezynski.Cinema.model.SoldTicket;
import net.biezynski.Cinema.model.SoldTicketDto;
import net.biezynski.Cinema.model.TicketDto;
import net.biezynski.Cinema.request.BookTicketRequest;
import net.biezynski.Cinema.request.BookTicketWithDiscountRequest;
import net.biezynski.Cinema.request.BookTicketsWithSameRow;
import net.biezynski.Cinema.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.nio.file.NotDirectoryException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/ticket")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Sell one ticket", description = "Sell one ticket, pass movie id and seat id")
    @PostMapping("sell")
    public TicketDto sellTicket(
            @RequestBody @Valid BookTicketRequest bookTicketRequest
    ) throws NotFoundFilmException, OccupiedOrNotExistSeat {
        try{
            return ticketService.bookOneTicket(bookTicketRequest);
        }catch (NotFoundFilmException notFoundFilmException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundFilmException.getMessage());
        }catch (OccupiedOrNotExistSeat occupiedOrNotExistSeat){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, occupiedOrNotExistSeat.getMessage());
        }
    }

    @Operation(summary = "Sell one ticket with some discount", description = "Sell one ticket with some discount, pass movie id, seat id and discount as integer")
    @PostMapping("sell/discount")
    public TicketDto sellTicketWithDiscount(
            @RequestBody @Valid BookTicketWithDiscountRequest bookTicketWithDiscountRequest
    ) throws NotFoundFilmException, OccupiedOrNotExistSeat {
        try{
            return ticketService.bookOneTicketWithDiscount(bookTicketWithDiscountRequest);
        }catch (NotFoundFilmException notFoundFilmException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundFilmException.getMessage());
        }catch (OccupiedOrNotExistSeat occupiedOrNotExistSeat){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, occupiedOrNotExistSeat.getMessage());
        }
    }

    @Operation(summary = "Get info about Tickets for given movie", description = "Get info about Tickets for given movie")
    @GetMapping("getAll/movie/{id}")
    public List<TicketDto> getAllTicketsForMovie(
            @PathVariable Long id
    ) throws NotFoundFilmException {
        try{
            return ticketService.getAllTicketsForMovie(id);
        }catch (NotFoundFilmException notFoundFilmException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundFilmException.getMessage());
        }
    }
    //not very clean how should it work
    //i implement it by give letter from row (eg: "B") and i find if there is N free seat next to each other in given row
    @Operation(summary = "Sell given N tickets next to each other", description = "Sell N multiple tickets (assign N new Seat Numbers in the same row if possible, eg. A5, A6, A7), find first possible sequence")
    @PostMapping("sell/tickets/sequence")
    public List<TicketDto> sellSequenceOfTicketsNextToEachOther(
            @RequestBody BookTicketsWithSameRow bookTicketsWithSameRow
    ) throws NotFoundFilmException {
        try{
            return ticketService.bookTicketsWithSameRow(bookTicketsWithSameRow);
        }catch (NotFoundFilmException notFoundFilmException){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundFilmException.getMessage());
        }
    }

    // List all tickets that have been sold with any discount; sort results by percentage discount value,
    @Operation(summary = "")
    @GetMapping("/getAll/withDiscount")
    public List<SoldTicketDto> getAllSoldTicketWithDiscount(){
        return ticketService.getAllSoldTicketWithDiscount();
    }
}
