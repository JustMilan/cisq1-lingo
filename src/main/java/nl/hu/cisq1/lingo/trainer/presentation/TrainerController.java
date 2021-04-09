package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.exception.*;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameResponseDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GuessDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.ResponseDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.TrainerResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    private final TrainerService service;

    public TrainerController(TrainerService service) {
        this.service = service;
    }

    @GetMapping("/game/all")
    ResponseDTO allGames() {
        return new GameResponseDTO(service.allGames());
    }

    @PostMapping("/game/start")
    ResponseDTO startGame() {
        return new TrainerResponseDTO(service.startNewGame());
    }


    @PostMapping("/game/startNewRound")
    ResponseDTO startNewRound(@RequestParam Long gameId) {
        try {
            return new TrainerResponseDTO(service.startNewRound(gameId));
        } catch (ActiveRoundException e) {
            return new ExceptionDTO(e.getMessage());
        }
    }

    @PostMapping("/game/guess")
    ResponseDTO guess(@RequestBody GuessDTO guess, @RequestParam Long gameId) {
        try {
            return new TrainerResponseDTO(service.guess(gameId, guess.guess));
        } catch (Exception e) {
            return new ExceptionDTO(e.getMessage());
        }
    }
}
