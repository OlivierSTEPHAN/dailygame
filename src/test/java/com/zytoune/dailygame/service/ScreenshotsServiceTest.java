package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Screenshots;
import com.zytoune.dailygame.model.ImageType;
import com.zytoune.dailygame.repository.games.ScreenshotsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ScreenshotsServiceTest {
    @Mock
    private ScreenshotsRepository screenshotsRepository;

    @InjectMocks
    private ScreenshotsService screenshotsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnScreenshotsWhenIdExists() {
        int id = 1;
        Screenshots screenshot = new Screenshots();
        when(screenshotsRepository.findById(id)).thenReturn(Optional.of(screenshot));

        Screenshots result = screenshotsService.getScreenshotsById(id);

        assertEquals(screenshot, result);
    }

    @Test
    void shouldReturnNullWhenIdDoesNotExist() {
        int id = 1;
        when(screenshotsRepository.findById(id)).thenReturn(Optional.empty());

        Screenshots result = screenshotsService.getScreenshotsById(id);

        assertNull(result);
    }

    @Test
    void shouldReturnCountOfScreenshots() {
        long count = 5L;
        when(screenshotsRepository.count()).thenReturn(count);

        long result = screenshotsService.count();

        assertEquals(count, result);
    }

    @Test
    void shouldGenerateUrlForImageId1080P() {
        String imageId = "abc123";
        String expectedUrl = "https://images.igdb.com/igdb/image/upload/t_1080p/" + imageId + ".jpg";

        String result = screenshotsService.generateUrl(imageId, ImageType.P1080);

        assertEquals(expectedUrl, result);
    }

    @Test
    void shouldGenerateUrlForImageId720P() {
        String imageId = "abc123";
        String expectedUrl = "https://images.igdb.com/igdb/image/upload/t_720p/" + imageId + ".jpg";

        String result = screenshotsService.generateUrl(imageId, ImageType.P720);

        assertEquals(expectedUrl, result);
    }
}