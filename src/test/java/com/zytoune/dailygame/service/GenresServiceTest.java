package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Genres;
import com.zytoune.dailygame.repository.games.GenresRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GenresServiceTest {

    @Mock
    private GenresRepository genresRepository;

    @InjectMocks
    private GenresService genresService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnGenresWhenIdsProvided() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<Genres> genres = Arrays.asList(new Genres(1,"genre1","slug"), new Genres(2,"genre2","slug"));
        when(genresRepository.findAllById(ids)).thenReturn(genres);

        List<String> result = genresService.getGenresByGenreIds(ids);

        assertEquals(2, result.size());
        assertEquals("genre1", result.get(0));
        assertEquals("genre2", result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenIdsAreNull() {
        List<String> result = genresService.getGenresByGenreIds(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingIds() {
        List<Integer> ids = Arrays.asList(3, 4);
        when(genresRepository.findAllById(ids)).thenReturn(List.of());

        List<String> result = genresService.getGenresByGenreIds(ids);

        assertTrue(result.isEmpty());
    }
}