package nl.hu.cisq1.lingo.trainer.presentation.dto;

import nl.hu.cisq1.lingo.trainer.domain.Game;

import java.util.List;

public class GameResponseDTO implements ResponseDTO {
    private final List<Game> games;

    public GameResponseDTO(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }
}
