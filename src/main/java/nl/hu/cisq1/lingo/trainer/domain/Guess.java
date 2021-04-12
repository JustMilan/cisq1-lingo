package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidCharacterException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Guess {
    @Id
    @GeneratedValue
    private Long id;

    private String wordattempt;

    public Guess() {}

    public Guess(String wordattempt) {
        char[] chars = wordattempt.toCharArray();
        for (char character : chars) {
            if (!Character.isLetter(character)) {
                throw new InvalidCharacterException();
            }
        }
        this.wordattempt = wordattempt;
    }

    public String getWordattempt() {
        return wordattempt;
    }

    private boolean composedOfLetters(String guess) {
        char[] chars = guess.toCharArray();
        for (char character : chars) {
            if (!Character.isLetter(character)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateLength(String guess, String word) {
        return guess.length() == word.length();
    }

    public boolean validateGuess(String guess, String word) {
        return composedOfLetters(guess) && validateLength(guess, word);
    }
}
