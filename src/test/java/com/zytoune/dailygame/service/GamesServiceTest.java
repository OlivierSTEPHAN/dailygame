package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Games;
import com.zytoune.dailygame.repository.games.GamesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GamesServiceTest {

    @Mock
    private GamesRepository gamesRepository;

    @InjectMocks
    private GamesService gamesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnNRandomGames() {
        Games game = mock(Games.class);
        when(gamesRepository.findNRandomGames(anyInt())).thenReturn(Arrays.asList(game, game, game));

        List<Games> result = gamesService.getNRandomGames(3);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void shouldReturnGameNames() {
        when(gamesRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(Arrays.asList("game1", "game2"));

        List<String> result = gamesService.findGameNames("game");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnNumberOfGames() {
        when(gamesRepository.count()).thenReturn(5L);

        long result = gamesService.getNbrGames();

        assertEquals(5, result);
    }

    @Test
    void shouldReturnGameByGameName() {
        Games game = mock(Games.class);
        when(gamesRepository.findByName(anyString())).thenReturn(Optional.of(game));

        Games result = gamesService.findGameByGameName("gameName");

        assertNotNull(result);
    }

    @Test
    void shouldReturnNullWhenGameNameNotFound() {
        when(gamesRepository.findByName(anyString())).thenReturn(Optional.empty());

        Games result = gamesService.findGameByGameName("gameName");

        assertNull(result);
    }

    @Test
    void shouldReturnNRandomGamesWhenNIsPositive() {
        Games game = mock(Games.class);
        when(gamesRepository.findNRandomGames(3)).thenReturn(Arrays.asList(game, game, game));

        List<Games> result = gamesService.findNRandomGames(3);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNIsZero() {
        when(gamesRepository.findNRandomGames(0)).thenReturn(Collections.emptyList());

        List<Games> result = gamesService.findNRandomGames(0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}