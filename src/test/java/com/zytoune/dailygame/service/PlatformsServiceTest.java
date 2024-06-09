package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Platforms;
import com.zytoune.dailygame.repository.games.PlatformsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PlatformsServiceTest {
    @Mock
    private PlatformsRepository platformsRepository;

    @InjectMocks
    private PlatformsService platformsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnPlatformsWhenIdsProvided() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<Platforms> platforms = Arrays.asList(new Platforms(1,"abbreviation1","alternativeName1",1,"name1",1,"summary1"), new Platforms(2,"abbreviation2","alternativeName2",2,"name2",2,"summary2"));
        when(platformsRepository.findAllById(ids)).thenReturn(platforms);

        List<String> result = platformsService.getPlatformsByIds(ids);

        assertEquals(2, result.size());
        assertEquals("name1", result.get(0));
        assertEquals("name2", result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenIdsAreNull() {
        List<String> result = platformsService.getPlatformsByIds(null);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoMatchingIds() {
        List<Integer> ids = Arrays.asList(3, 4);
        when(platformsRepository.findAllById(ids)).thenReturn(List.of());

        List<String> result = platformsService.getPlatformsByIds(ids);

        assertTrue(result.isEmpty());
    }
}