package net.biezynski.Cinema.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AddMovieRequest {
    @NotNull
    private String movieName;
    @NotNull
    private String description;
    @NotNull
    private LocalDateTime dateString;
    @NotNull
    private int movieLength;
    @NotNull
    private BigDecimal priceOfTicket;
}
