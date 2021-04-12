package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.exception.NoGameFoundException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    private WordService wordService;
    private Game game;
    private TrainerService trainerService;
    private SpringGameRepository repository;

    @BeforeEach
    void initialize() {
        this.game = mock(Game.class);
        this.repository = mock(SpringGameRepository.class);
        this.wordService = mock(WordService.class);
        this.trainerService = new TrainerService(wordService, repository);

        when(this.wordService.provideRandomWord((anyInt()))).thenReturn("pizza");
        when(this.trainerService.startNewGame()).thenReturn(this.game);
        when(this.trainerService.allGames()).thenReturn(List.of(this.game));
        when(this.repository.save(any())).thenReturn(game);
        when(this.repository.findById(1L)).thenReturn(Optional.of(game));
        when(this.game.getId()).thenReturn(1L);
    }

    @AfterEach
    void teardown() {
        clearInvocations(wordService, repository);
    }

    @ParameterizedTest
    @DisplayName("Wordservice always provides a word")
    @MethodSource("wordserviceExamples")
    void provideRandomWord(int length) {
        assertEquals("pizza", wordService.provideRandomWord(length));
    }

    @Test
    @DisplayName("start a new game shouldn't throw error and return a game")
    void startNewGame() {
        assertNotNull(trainerService.startNewGame());
    }

    @Test
    @DisplayName("get gameId is correct")
    void getGameId() {
        assertEquals(1L, game.getId());
    }

    @Test
    @DisplayName("Game Does not exist when guess on non existing game")
    void nonExistingGameGuess() {
        assertThrows(NoGameFoundException.class, () -> trainerService.guess(2L, "pizza"));
    }

    @Test
    @DisplayName("new round should return game")
    void newRound() {
        assertNotNull(trainerService.startNewRound(1L));
    }

    @Test
    @DisplayName("guess should return game")
    void guess() {
        assertNotNull(trainerService.guess(1L, "angel"));
    }

    private static Stream<Arguments> wordserviceExamples() {
        return Stream.of(Arguments.of(5), Arguments.of(6), Arguments.of(7));
    }
}