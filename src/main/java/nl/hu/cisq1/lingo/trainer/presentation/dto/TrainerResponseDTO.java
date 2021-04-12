package nl.hu.cisq1.lingo.trainer.presentation.dto;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameState;
import nl.hu.cisq1.lingo.trainer.domain.Hint;

public class TrainerResponseDTO implements ResponseDTO {
    private final Long id;
    private final Hint hint;
    private final GameState gameState;
    private final Integer guessAmount;
    private final int score;

    public TrainerResponseDTO(Game game) {
        this.id = game.getId();
        this.hint = game.getRound().getHint();
        this.gameState = game.getGameState();
        this.guessAmount = game.getRound().getGuesses().size();
        this.score = game.getPoints();
    }

    public TrainerResponseDTO(Long id, Hint hint, GameState gameState, Integer guessAmount, Feedback feedback, int score) {
        this.id = id;
        this.hint = hint;
        this.gameState = gameState;
        this.guessAmount = guessAmount;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public Hint getHint() {
        return hint;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Integer getGuessAmount() {
        return guessAmount;
    }

    public int getScore() {
        return score;
    }
}
