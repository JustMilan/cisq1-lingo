package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.presentation.dto.ResponseDTO;

public class ExceptionDTO implements ResponseDTO {
    private final String exception;

    public ExceptionDTO(String e) {
        this.exception = e;
    }

    public String getException() {
        return exception;
    }
}
