package net.biezynski.Cinema.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String movieName;
    private String description;
    private LocalDateTime dateString;

    @OneToMany(mappedBy = "movie",cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JsonManagedReference
    private List<Ticket> tickets;
    private int movieLength;
}