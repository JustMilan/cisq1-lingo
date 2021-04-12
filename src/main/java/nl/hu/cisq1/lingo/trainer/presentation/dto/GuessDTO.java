package nl.hu.cisq1.lingo.trainer.presentation.dto;

import javax.validation.constraints.NotBlank;

public class GuessDTO implements ResponseDTO {
    @NotBlank
    public String guess;
}
