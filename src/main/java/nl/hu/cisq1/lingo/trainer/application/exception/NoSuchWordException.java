package nl.hu.cisq1.lingo.trainer.application.exception;

public class NoSuchWordException extends RuntimeException {
    public NoSuchWordException(String word) {
        super(String.format("%s is a non existing array of characters", word));
    }
}
