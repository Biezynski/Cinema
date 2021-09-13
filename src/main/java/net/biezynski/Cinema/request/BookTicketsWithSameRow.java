package net.biezynski.Cinema.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BookTicketsWithSameRow {
    @NotNull
    private Long movieId;
    @NotNull
    private String rowLetter;
    @NotNull
    private int numberOfSeats;
}
