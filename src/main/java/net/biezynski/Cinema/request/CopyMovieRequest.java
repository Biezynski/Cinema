package net.biezynski.Cinema.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CopyMovieRequest {
    @NotNull
    private LocalDateTime dateString;
}
