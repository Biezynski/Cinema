package net.biezynski.Cinema.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BookTicketRequest {
    @NotNull
    private Long movieId;
    @NotNull
    private String seatId;
}
