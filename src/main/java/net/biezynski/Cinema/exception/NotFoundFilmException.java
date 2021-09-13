package net.biezynski.Cinema.exception;


public class NotFoundFilmException extends Exception {
    public NotFoundFilmException(Long id) {
        super("Not Found film with given id:" + id);
    }
}
