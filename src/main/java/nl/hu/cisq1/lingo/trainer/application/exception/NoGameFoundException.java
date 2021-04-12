package nl.hu.cisq1.lingo.trainer.application.exception;

public class NoGameFoundException extends RuntimeException {
    public NoGameFoundException(Long id) {
        super(String.format("Game with id: \"%s\" has not been found", id));
    }
}
