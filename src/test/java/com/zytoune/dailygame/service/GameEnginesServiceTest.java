package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.GameEngines;
import com.zytoune.dailygame.repository.games.GameEnginesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GameEnginesServiceTest {

    @Mock
    private GameEnginesRepository gameEnginesRepository;

    @InjectMocks
    private GameEnginesService gameEnginesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnGameEnginesWhenIdsProvided() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<GameEngines> gameEngines = Arrays.asList(new GameEngines(1,"name1","slug"), new GameEngines(2,"name2","slug"));
        when(gameEnginesRepository.findAllById(ids)).thenReturn(gameEngines);

        List<String> result = gameEnginesService.getGameEngineById(ids);

        assertEquals(2, result.size());
        assertEquals("name1", result.get(0));
        assertEquals("name2", result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenIdsAreNull() {
        List<String> result = gameEnginesService.getGameEngineById(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingIds() {
        List<Integer> ids = Arrays.asList(3, 4);
        when(gameEnginesRepository.findAllById(ids)).thenReturn(List.of());

        List<String> result = gameEnginesService.getGameEngineById(ids);

        assertTrue(result.isEmpty());
    }
}