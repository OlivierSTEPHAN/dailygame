package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.AlternativeNames;
import com.zytoune.dailygame.repository.games.AlternativeNamesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AlternativeNamesServiceTest {
    @Mock
    private AlternativeNamesRepository alternativeNamesRepository;

    @InjectMocks
    private AlternativeNamesService alternativeNamesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAlternativeNames() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<AlternativeNames> alternativeNames = Arrays.asList(new AlternativeNames(1,"comment1", 1, "name1"), new AlternativeNames(2,"comment2", 2, "name2"));
        when(alternativeNamesRepository.findAllById(ids)).thenReturn(alternativeNames);

        List<String> result = alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(ids);

        assertEquals(2, result.size());
        assertEquals("name1", result.get(0));
        assertEquals("name2", result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenNoIdsProvided() {
        List<String> result = alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(List.of());

        assertTrue(result.isEmpty());
    }
}