package net.biezynski.Cinema.Utils;

import net.biezynski.Cinema.model.Movie;
import net.biezynski.Cinema.model.Ticket;
import net.biezynski.Cinema.repository.MovieRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utils {

    //generate seats from A to Z row and from 1 to 22 number of seat
    //in real app should be some dictionary with roomId and seats
    public static List<String> generateSeatsIds() {
        List<String> seatsNumber = new ArrayList<String>();
        String letter;
        String number;
        String concat;
        for (char i = 'A'; i <= 'Z'; i++) {
            for (int j = 1; j < 23; j++) {
                letter = String.valueOf(i);
                number = String.valueOf(j);
                concat = letter + number;
                seatsNumber.add(concat);
            }
        }
        return seatsNumber;
    }

   public static void generateStartData(MovieRepository movieRepository){
        List<Ticket> tickets = new ArrayList<>();
        Movie movie = Movie.builder()
                .movieName("Shrek")
                .dateString( LocalDateTime.of(LocalDate.now(), LocalTime.now()))
                .description("Bardzo dobry film")
                .movieLength(122)
                .build();
        Utils.generateSeatsIds().forEach(
                seatId -> {
                    Ticket newTicket = Ticket.builder()
                            .ticketNumber(UUID.randomUUID().toString())
                            .seatNumber(seatId)
                            .ticketPrice(new BigDecimal("29.99"))
                            .movie(movie)
                            .build();
                    tickets.add(newTicket);
                }
        );
        movie.setTickets(tickets);
        movieRepository.save(movie);
    }
}
