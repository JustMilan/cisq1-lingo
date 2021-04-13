package nl.hu.cisq1.lingo.trainer.presentation.dto;

public class ExceptionDTO implements ResponseDTO {
    private final String exception;

    public ExceptionDTO(String e) {
        this.exception = e;
    }

    public String getException() {
        return exception;
    }
}
