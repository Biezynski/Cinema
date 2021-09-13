package net.biezynski.Cinema.exception;

public class OccupiedOrNotExistSeat extends Exception {
    public OccupiedOrNotExistSeat(Long id) {
        super("Seat with id: "+ id + " does not exist or is already occupied by someone else");
    }
}
