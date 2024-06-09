package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.GameModes;
import com.zytoune.dailygame.repository.games.GameModesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GameModesServiceTest {

    @Mock
    private GameModesRepository gameModesRepository;

    @InjectMocks
    private GameModesService gameModesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnGameModesWhenIdsProvided() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<GameModes> gameModes = Arrays.asList(new GameModes(1,"mode1","slug"), new GameModes(2,"mode2","slug"));
        when(gameModesRepository.findAllById(ids)).thenReturn(gameModes);

        List<String> result = gameModesService.getGameModesByIds(ids);

        assertEquals(2, result.size());
        assertEquals("mode1", result.get(0));
        assertEquals("mode2", result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenIdsAreNull() {
        List<String> result = gameModesService.getGameModesByIds(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingIds() {
        List<Integer> ids = Arrays.asList(3, 4);
        when(gameModesRepository.findAllById(ids)).thenReturn(List.of());

        List<String> result = gameModesService.getGameModesByIds(ids);

        assertTrue(result.isEmpty());
    }
}