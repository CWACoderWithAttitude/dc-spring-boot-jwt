package io.github.cwacoderwithattitude.games.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cwacoderwithattitude.games.model.Game;
import io.github.cwacoderwithattitude.games.service.GameService;

@WebMvcTest(GameUploadController.class)
class GameUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @MockitoBean
    private io.github.cwacoderwithattitude.games.security.JwtUtil jwtUtil;

    @MockitoBean
    private io.github.cwacoderwithattitude.games.service.CustomUserDetailsService customUserDetailsService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Test
    void uploadGames_withJsonBody_shouldReturnOk() throws Exception {
        Game game = new Game();
        game.setTitle("Test Game");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Arrays.asList(game));

        // Mock the service method used by the controller
        // Mockito.when(gameService.saveAll(Mockito.anyList())).thenReturn(Arrays.asList(game));

        mockMvc.perform(post("/games/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Successfully uploaded")));
    }

    @Test
    void uploadGames_withNoInput_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/games/upload"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No file or JSON body provided")));
    }
}