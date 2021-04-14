package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.ActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameLostException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.RoundWonException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Round round;

    @OneToMany
    private final List<Round> rounds;

    @Column
    private int points;
    private GameState gameState;

    public Game() {
        this.rounds = new ArrayList<>();
        this.gameState = GameState.CONTINUE;
        this.points = 0;
    }

    public void newRound(String word) {
        switch (this.gameState) {
            case LOST -> throw new GameLostException();
            case WON -> handleWon(word);
            default -> handleContinue(word);
        }
    }

    private void handleContinue(String word) {
        if (this.round != null) {
            throw new ActiveRoundException();
        } else {
            this.round = new Round(word);
            this.gameState = GameState.CONTINUE;
        }
    }

    private void handleWon(String word) {
        endRound();
        this.round = new Round(word);
        this.gameState = GameState.CONTINUE;
    }

    public void makeGuess(String guess) {
        handleGameState();
        this.round.makeGuess(guess);
        checkGameState();
    }

    void handleGameState() {
        if (this.gameState == GameState.LOST) {
            endRound();
            throw new GameLostException();
        } else if (this.gameState == GameState.WON) {
            endRound();
            throw new RoundWonException();
        }
    }

    public void checkGameState() {
        this.gameState = this.round.getGameState();
    }

    public void calculateScore() {
        int score = 0;
        for (Round r : this.rounds) {
            if (r != null && r.getGameState() == GameState.WON) {
                score += 5 * (5 - r.getGuesses().size()) + 5;
            }
        }
        this.points = score;
    }

    public int provideNextLenghtWord() {
        if (this.round == null) {
            throw new NoActiveRoundException();
        }
        return switch (this.round.getWord().length()) {
            case 5 -> 6;
            case 6 -> 7;
            default -> 5;
        };
    }

    public void endRound() {
        this.rounds.add(round);
        this.round = null;
        calculateScore();
    }

    public int getPoints() {
        return points;
    }

    public Long getId() {
        return id;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Round getRound() {
        return round;
    }
}
