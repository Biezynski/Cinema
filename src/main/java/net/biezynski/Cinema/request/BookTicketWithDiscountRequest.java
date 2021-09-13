package net.biezynski.Cinema.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BookTicketWithDiscountRequest {
    @NotNull
    private Long movieId;
    @NotNull
    private String seatId;
    @NotNull
    private int discountInPercentage;
}
