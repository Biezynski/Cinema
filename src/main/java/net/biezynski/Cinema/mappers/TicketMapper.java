package net.biezynski.Cinema.mappers;

import net.biezynski.Cinema.model.SoldTicket;
import net.biezynski.Cinema.model.SoldTicketDto;
import net.biezynski.Cinema.model.Ticket;
import net.biezynski.Cinema.model.TicketDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel="spring")
public interface TicketMapper {
    @Mapping(target = "price", source = "ticketPrice")
    @Mapping(target = "movieName", source = "movie.movieName")
    TicketDto mapTicketToTicketDto(Ticket ticket);

    List<TicketDto> mapListTicketsToTicketsDto(List<Ticket> tickets);

    @Mapping(target = "discount", ignore = true)
    SoldTicket mapTicketToSoldTicket(Ticket ticket);

    SoldTicketDto mapSoldTicketToSoldTicketDto(SoldTicket soldTicket);

    List<SoldTicketDto> mapListSoldTicketToListSoldTicketDto(List<SoldTicket> soldTicket);

}
