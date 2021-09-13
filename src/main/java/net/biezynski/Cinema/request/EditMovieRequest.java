package net.biezynski.Cinema.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditMovieRequest {
    private String movieName;
    private String description;
    private LocalDateTime dateString;
    private int movieLength;
}
