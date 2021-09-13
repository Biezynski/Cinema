package net.biezynski.Cinema.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketDto {
    private Long id;
    private String ticketNumber;
    private String seatNumber;
    private String movieName;
    private BigDecimal price;
}
