package nl.hu.cisq1.lingo.trainer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameState;
import nl.hu.cisq1.lingo.trainer.domain.exception.ActiveRoundException;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GuessDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class TrainerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TrainerService service;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void initialize() {
        var controller = new TrainerController(this.service);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("wrong request returns 404")
    void wrongRequest() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/trainer/wrong");
        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("New game should not throw error")
    void newGameCorrect() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.post("/trainer/game/start");
        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("All games should return 200 always")
    void allGames() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/trainer/game/all");
        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("start a new round should without game ID should return bad request")
    void startNewRoundNoId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trainer/game/startNewRound"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("start a new round after gameLost should return error")
    void startNewRoundLost() throws Exception {
        var game = this.service.startNewGame();
        var guessDto = new GuessDTO();
        guessDto.guess = "kerel";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/trainer/game/start"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        for (int i = 0; i <= 5; i++) {
            this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("/trainer/game/guess?gameId=%d", game.getId()))
                    .content(this.objectMapper.writeValueAsString(guessDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("/trainer/game/startNewRound?id=%d", game.getId())))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("start a new round after round won should return 200 with exception message")
    void startNewRoundOk() throws Exception {
        Game game = this.service.startNewGame();
        GuessDTO guessDto = new GuessDTO();
        guessDto.guess = game.getRound().getWord();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/trainer/game/start"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("/trainer/game/guess?gameId=%d", game.getId()))
                .content(this.objectMapper.writeValueAsString(guessDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("/trainer/game/startNewRound?gameId=%d", game.getId())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("active round test to be thrown when there is still an active round")
    void activeRoundException() throws Exception {
        Game game = this.service.startNewGame();

        this.mockMvc.perform(MockMvcRequestBuilders.post("/trainer/game/start"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("/trainer/game/startNewRound?gameId=%d", game.getId())))
                .andExpect(status().isOk());

        Long gameId = game.getId();
        assertThrows(ActiveRoundException.class, () -> service.startNewRound(gameId));
    }
}