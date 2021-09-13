package net.biezynski.Cinema.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SoldTicketDto {
    private Long id;
    private String ticketNumber;
    private String seatNumber;
    private BigDecimal ticketPrice;
    private int discount;
}
