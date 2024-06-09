package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.PlayerPerspectives;
import com.zytoune.dailygame.repository.games.PlayerPerspectivesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PlayerPerspectivesServiceTest {
    @Mock
    private PlayerPerspectivesRepository playerPerspectivesRepository;

    @InjectMocks
    private PlayerPerspectivesService playerPerspectivesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPlayerPerspectivesWhenIdsProvided() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<PlayerPerspectives> playerPerspectives = Arrays.asList(new PlayerPerspectives(1,"perspective1","slug"), new PlayerPerspectives(2,"perspective2","slug"));
        when(playerPerspectivesRepository.findAllById(ids)).thenReturn(playerPerspectives);

        List<String> result = playerPerspectivesService.getPlayerPerspectivesByIds(ids);

        assertEquals(2, result.size());
        assertEquals("perspective1", result.get(0));
        assertEquals("perspective2", result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenIdsAreNull() {
        List<String> result = playerPerspectivesService.getPlayerPerspectivesByIds(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingIds() {
        List<Integer> ids = Arrays.asList(3, 4);
        when(playerPerspectivesRepository.findAllById(ids)).thenReturn(List.of());

        List<String> result = playerPerspectivesService.getPlayerPerspectivesByIds(ids);

        assertTrue(result.isEmpty());
    }
}