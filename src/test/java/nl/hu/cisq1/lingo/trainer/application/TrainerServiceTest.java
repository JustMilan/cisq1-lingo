package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameState;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

class TrainerServiceTest {
    WordService wordService = mock(WordService.class);
    SpringGameRepository repository = mock(SpringGameRepository.class);
    TrainerService service;
    Game game;

    @BeforeEach
    void initialize() {
        game = new Game();
        when(wordService.provideRandomWord(5)).thenReturn("anker");
        when(repository.findById(1L)).thenReturn(Optional.of(game));
        service = new TrainerService(wordService, repository);
    }

    @AfterEach
    void teardown() {
        clearInvocations(wordService, repository);
    }

    @Test
    @DisplayName("When starting a game, we should return a game object")
    void testStartGameShouldReturnGame() {
        Game game = service.startNewGame();

        verify(wordService, times(1)).provideRandomWord(5);
        verify(repository, times(1)).save(any(Game.class));
        assertEquals(GameState.CONTINUE, game.getGameState());
    }

}