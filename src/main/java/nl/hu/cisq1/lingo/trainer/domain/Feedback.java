package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.character.MarkedLingoCharacter;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidCharacterException;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidGuessLengthException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * The Feedback class provides feedback in a treemap<index of char, MarkedLingoCharacter> format.
 * The TreeMap is convertible to a list of chars and a list of chars.
 */
@Entity
public class Feedback {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String attempt;

    @ElementCollection
    private List<Mark> marks;

    public Feedback() {}
    public Feedback(String attempt) {
        this.attempt = attempt;
        this.marks = new ArrayList<>();
    }

    public Feedback(List<Mark> marks, String attempt) {
        if (isGuessValid(marks)) {
            this.marks = marks;
        } else {
            throw new InvalidCharacterException();
        }

        if (attempt.length() == marks.size()) {
            this.attempt = attempt;
        } else {
            throw new InvalidGuessLengthException();
        }
    }

    public boolean isWordGuessed() {
        return this.marks.stream().allMatch(mark -> mark == Mark.CORRECT);
    }

    public boolean isGuessValid(List<Mark> marks) {
        return !marks.isEmpty() && !marks.contains(Mark.INVALID);
    }

    private boolean isPresent(char c, char[] cWord) {
        for (char value : cWord) {
            if (c == value) {
                return true;
            }
        }
        return false;
    }

    private int charOccuences(char c, char[] wordArray) {
        int counter = 0;
        for (char value : wordArray) {
            if (value == c) {
                counter++;
            }
        }
        return counter;
    }

    private boolean occurenceReached(TreeMap<Integer, MarkedLingoCharacter> guess, char c, int charOccurence) {
        int inGuess = 0;
        for (int i = 0; i < guess.size(); i++) {
            if (guess.get(i).getMark() == null) {
                continue;
            }
            if (c == guess.get(i).getCharacter()) {
                inGuess++;
            }
        }

        if (charOccurence == 1 && inGuess == 1) {
            return false;
        }

        return inGuess >= charOccurence;
    }

    public SortedMap<Integer, MarkedLingoCharacter> prepareFeedback(String actual, String guess) {
        char[] actualArray = actual.toCharArray();
        char[] guessArray = guess.toCharArray();
        TreeMap<Integer, MarkedLingoCharacter> feedback = new TreeMap<>();

        for (int i = 0; i < actualArray.length; i++) {
            if (actualArray[i] == guessArray[i]) {
                feedback.put(i, new MarkedLingoCharacter(guessArray[i], Mark.CORRECT));
            } else {
                feedback.put(i, new MarkedLingoCharacter(guessArray[i]));
            }
        }

        for (int i = 0; i < actualArray.length; i++) {
            if (feedback.get(i).getMark() != Mark.CORRECT) {
                feedback.put(i, new MarkedLingoCharacter(guessArray[i], Mark.TURN));
                boolean present = isPresent(guessArray[i], actualArray);
                int charOccurence = charOccuences(guessArray[i], actualArray);
                boolean occurenceReached = occurenceReached(feedback, guessArray[i], charOccurence);

                if (!present || occurenceReached) {
                    feedback.put(i, new MarkedLingoCharacter(guessArray[i], Mark.ABSENT));
                    continue;
                }

                feedback.put(i, new MarkedLingoCharacter(guessArray[i], Mark.PRESENT));

            }
        }
        return feedback;
    }

    /* Converts the Treemap from the prepareFeedback method to an ArrayList that only consists of Marks. */
    public List<Mark> toMarkArray(SortedMap<Integer, MarkedLingoCharacter> feedback) {
        ArrayList<Mark> marksList = new ArrayList<>();
        feedback.values().forEach(i -> marksList.add(i.getMark()));
        return marksList;
    }

    public List<Character> toCharArrayList(SortedMap<Integer, MarkedLingoCharacter> feedback) {
        List<Character> chars = new ArrayList<>();
        feedback.values().forEach(i -> chars.add(i.getCharacter()));
        return chars;
    }

    public Hint giveHint(Hint previousHint, String word) {
        List<Character> hint = new ArrayList<>();
        char[] cWord = word.toCharArray();
        for (int i = 0; i < cWord.length; i++) {
            switch (this.marks.get(i)) {
                case CORRECT -> hint.add(cWord[i]);
                case PRESENT -> hint.add('+');
                default -> hint.add('-');
            }
        }
        previousHint.setHint(hint);
        return previousHint;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public void setMarks(List<Mark> marks) {
        this.marks = marks;
    }

    public Long getId() {
        return id;
    }
}
