package io.github.cwacoderwithattitude.games.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import io.github.cwacoderwithattitude.games.model.Game;
import io.github.cwacoderwithattitude.games.service.GameService;
import io.github.cwacoderwithattitude.games.service.SeedDataReader;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

@WebMvcTest(GameController.class)
@Import(GameControllerTest.TestConfig.class)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private SeedDataReader seedDataReader;

    @MockBean
    private io.github.cwacoderwithattitude.games.security.JwtUtil jwtUtil;

    @MockBean
    private io.github.cwacoderwithattitude.games.service.CustomUserDetailsService customUserDetailsService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Test
    void getGames_shouldReturnList() throws Exception {
        Game game = new Game();
        game.setTitle("Test Game");
        when(gameService.list()).thenReturn(Collections.singletonList(game));

        mockMvc.perform(get("/games/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Game"));
    }

    @Test
    void getGameById_shouldReturnGame() throws Exception {
        Game game = new Game();
        game.setTitle("Test Game");
        when(gameService.getById(1L)).thenReturn(game);

        mockMvc.perform(get("/games/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Game"));
    }

    @Test
    void getGameById_shouldReturnNotFound() throws Exception {
        when(gameService.getById(2L)).thenReturn(null);

        mockMvc.perform(get("/games/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_shouldReturnUpdatedGame() throws Exception {
        Game game = new Game();
        game.setTitle("Updated Game");
        when(gameService.update(eq(1L), any(Game.class))).thenReturn(game);

        String data = "{\"id\": 11602,\"title\": \"Die Siedler von Mittelerde, Updated2\"}";

        mockMvc.perform(put("/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Game"));
    }

    @Test
    void save_shouldReturnSavedGame() throws Exception {
        Game game = new Game();
        game.setTitle("New Game");
        when(gameService.save(any(Game.class))).thenReturn(game);

        mockMvc.perform(post("/games/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Game\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Game"));
    }
}