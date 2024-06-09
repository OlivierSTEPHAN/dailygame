package com.zytoune.dailygame.controller;

import com.zytoune.dailygame.entity.games.Games;
import com.zytoune.dailygame.service.GamesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GamesControllerTest {

    @Mock
    private GamesService gamesService;

    @InjectMocks
    private GamesController gamesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnNRandomGames() {
        List<Games> gamesList = Arrays.asList(new Games(), new Games());
        when(gamesService.getNRandomGames(2)).thenReturn(gamesList);

        ResponseEntity<List<Games>> responseEntity = gamesController.getNRandomGames(2);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(gamesList, responseEntity.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoRandomGames() {
        when(gamesService.getNRandomGames(2)).thenReturn(Arrays.asList());

        ResponseEntity<List<Games>> responseEntity = gamesController.getNRandomGames(2);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(0, responseEntity.getBody().size());
    }

    @Test
    void shouldReturnGameNamesForAutocomplete() {
        List<String> gameNames = Arrays.asList("game1", "game2");
        when(gamesService.findGameNames("game")).thenReturn(gameNames);

        List<String> response = gamesController.autocomplete("game");

        assertEquals(gameNames, response);
    }

    @Test
    void shouldReturnEmptyListWhenNoGameNamesForAutocomplete() {
        when(gamesService.findGameNames("game")).thenReturn(Arrays.asList());

        List<String> response = gamesController.autocomplete("game");

        assertEquals(0, response.size());
    }
}