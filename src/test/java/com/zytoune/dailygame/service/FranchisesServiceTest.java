package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Franchises;
import com.zytoune.dailygame.repository.games.FranchisesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FranchisesServiceTest {
    @Mock
    private FranchisesRepository franchisesRepository;

    @InjectMocks
    private FranchisesService franchisesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnFranchisesWhenIdsProvided() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<Franchises> franchises = Arrays.asList(new Franchises(1,"name1","slug"), new Franchises(2,"name2","slug"));
        when(franchisesRepository.findAllById(ids)).thenReturn(franchises);

        List<String> result = franchisesService.getFranchisesByIds(ids);

        assertEquals(2, result.size());
        assertEquals("name1", result.get(0));
        assertEquals("name2", result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenIdsAreNull() {
        List<String> result = franchisesService.getFranchisesByIds(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingIds() {
        List<Integer> ids = Arrays.asList(3, 4);
        when(franchisesRepository.findAllById(ids)).thenReturn(List.of());

        List<String> result = franchisesService.getFranchisesByIds(ids);

        assertTrue(result.isEmpty());
    }
}