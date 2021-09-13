package net.biezynski.Cinema.service;

import lombok.RequiredArgsConstructor;
import net.biezynski.Cinema.repository.MovieRepository;
import net.biezynski.Cinema.repository.SoldTicketRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CleanManager {

    private final MovieRepository movieRepository;
    private final SoldTicketRepository soldTicketRepository;

    @Scheduled(cron = "0 15 10 15 * ?") //TODO fix cron
    public void deleteAllFilmAtTheEndOfDay() {
        movieRepository.deleteAll();
    }

    @Scheduled(cron = "0 15 10 15 * ?") //TODO fix cron
    public void deleteSoldTicketsOver30days() {
        soldTicketRepository.deleteAll();
    }
}
