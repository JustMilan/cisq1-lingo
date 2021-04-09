package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.application.exception.NoGameFoundException;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {
    private final WordService wordService;
    private final SpringGameRepository gameRepository;

    public TrainerService(WordService wordService, SpringGameRepository gameRepository) {
        this.wordService = wordService;
        this.gameRepository = gameRepository;
    }

    public List<Game> allGames() {
        return gameRepository.findAll();
    }

    public Game startNewGame() {
        Game game = new Game();
        game.newRound(wordService.provideRandomWord(5));
        return gameRepository.save(game);
    }

    public Game startNewRound(Long gameId) {
        var game = gameRepository.findById(gameId).orElseThrow(() -> new NoGameFoundException(gameId));
        game.newRound(this.wordService.provideRandomWord(game.provideNextLenghtWord()));
        return gameRepository.save(game);
    }

    public Game guess(Long gameId, String guess) {
        var game = gameRepository.findById(gameId).orElseThrow(() -> new NoGameFoundException(gameId));
        game.makeGuess(guess);
        return gameRepository.save(game);
    }
}
