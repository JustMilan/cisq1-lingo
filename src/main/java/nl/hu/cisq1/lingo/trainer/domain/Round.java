package nl.hu.cisq1.lingo.trainer.domain;

import lombok.SneakyThrows;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameLostException;
import nl.hu.cisq1.lingo.trainer.domain.exception.RoundWonException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Round {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Hint hint;
    private String word;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Feedback> feedbackList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Guess> guesses = new ArrayList<>();
    private GameState gameState;

    public Round() {
    }

    public Round(String word) {
        this.word = word;
        this.feedbackList = new ArrayList<>();
        this.hint = giveFirstHint();
        this.guesses = new ArrayList<>(word.length());
        this.gameState = GameState.CONTINUE;
    }

    public Hint giveFirstHint() {
        ArrayList<Character> chars = new ArrayList<>();
        chars.add(this.word.charAt(0));
        for (int i = 0; i < word.length() - 1; i++) {
            chars.add('-');
        }
        return new Hint(chars);
    }

    @SneakyThrows
    public void makeGuess(String guess) {
        Guess attempt = new Guess(guess);
        if (attempt.validateGuess(guess, this.word)) {
            switch (this.gameState) {
                case LOST -> throw new GameLostException();
                case WON -> throw new RoundWonException();
                default -> handleValidGuess(attempt, guess);
            }
        }
    }

    private void handleValidGuess(Guess attempt, String guess) {
        this.guesses.add(attempt);
        Feedback feedback = new Feedback(guess);
        List<Mark> marks = feedback.toMarkArray(feedback.prepareFeedback(this.word, guess));
        feedback.setMarks(marks);
        feedbackList.add(feedback);
        hint.giveHint(guesses, feedbackList, word);
        determineState(feedback);
    }

    public void determineState(Feedback feedback) {
        if (feedback.isWordGuessed()) {
            this.gameState = GameState.WON;
        } else if (this.guesses.size() > 4) {
            this.gameState = GameState.LOST;
        } else {
            this.gameState = GameState.CONTINUE;
        }
    }

    public Hint getHint() {
        return hint;
    }

    public String getWord() {
        return word;
    }

    public List<Feedback> getFeedbackList() {
        return feedbackList;
    }

    public List<Guess> getGuesses() {
        return guesses;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Long getId() {
        return id;
    }
}
