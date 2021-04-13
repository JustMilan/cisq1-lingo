package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exception.ActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameLostException;
import nl.hu.cisq1.lingo.trainer.domain.exception.RoundWonException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TrainerServiceIntegrationTest {
    @Autowired
    private TrainerService service;

    @Autowired
    private SpringGameRepository repository;

    @Autowired
    private WordService wordService;

    private Game game;
    private Long gameId;

    @BeforeEach
    void initialize() {
        this.service = new TrainerService(wordService, repository);
        this.game = service.startNewGame();
        this.gameId = game.getId();
    }

    @Test
    @DisplayName("start game should return game")
    void startNewGame() {
        assertNotNull(service.startNewGame());
    }

    @Test
    @DisplayName("start new should return activeRoundException")
    void activeRoundException() {
        assertThrows(ActiveRoundException.class, () -> service.startNewRound(gameId));
    }

    @Test
    @DisplayName("start new round after won should throw round won exception")
    void roundWonException() {
        String word = game.getRound().getWord();
        service.guess(gameId, word);
        assertThrows(RoundWonException.class, () -> service.guess(gameId, word));
    }

    @Test
    @DisplayName("get all games should return games")
    void getAllGames() {
        assertNotNull(service.allGames());
    }

    @Test
    @DisplayName("guess after game lost should throw exception")
    void gameLostException() {
        String word = game.getRound().getWord();
        if (word.charAt(4) != word.charAt(1)) {
            word = word.replace(word.charAt(4), word.charAt(1));
        } else if (word.charAt(3) != word.charAt(1)) {
            word = word.replace(word.charAt(3), word.charAt(1));
        } else if (word.charAt(2) != word.charAt(1)) {
            word = word.replace(word.charAt(2), word.charAt(1));
        }
        service.guess(gameId, word);
        service.guess(gameId, word);
        service.guess(gameId, word);
        service.guess(gameId, word);
        service.guess(gameId, word);
        String finalWord = word;
        assertThrows(GameLostException.class, () -> service.guess(gameId, finalWord));
    }
}