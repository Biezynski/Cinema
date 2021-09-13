package net.biezynski.Cinema.model;

import lombok.Data;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieDto {
    private Long id;
    private String movieName;
    private String description;
    private String dateString;
    private List<Ticket> tickets;
    private int movieLength;
}