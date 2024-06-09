package com.zytoune.dailygame.service;

import com.zytoune.dailygame.dto.DailyGamesScreenshotsDTO;
import com.zytoune.dailygame.entity.games.DailyGamesScreenshot;
import com.zytoune.dailygame.entity.games.Games;
import com.zytoune.dailygame.entity.games.Screenshots;
import com.zytoune.dailygame.repository.games.DailyGamesScreenshotsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DailyGamesScreenshotServiceTest {
    @Mock
    private DailyGamesScreenshotsRepository dailyGamesScreenshotsRepository;

    @Mock
    private GamesService gamesService;

    @Mock
    private ScreenshotsService screenshotsService;

    @Mock
    private AlternativeNamesService alternativeNamesService;

    @InjectMocks
    private DailyGamesScreenshotService dailyGamesScreenshotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnDailyGamesWhenExist() {
        List<DailyGamesScreenshot> dailyGamesScreenshots = Arrays.asList(new DailyGamesScreenshot(), new DailyGamesScreenshot());
        when(dailyGamesScreenshotsRepository.findAll()).thenReturn(dailyGamesScreenshots);

        DailyGamesScreenshotsDTO result = dailyGamesScreenshotService.getDailyGames();

        assertEquals(2, result.getScreenshots().size());
    }

    @Test
    void shouldThrowExceptionWhenNoDailyGames() {
        when(dailyGamesScreenshotsRepository.findAll()).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> dailyGamesScreenshotService.getDailyGames());
    }

    @Test
    void shouldReturnTrueWhenAnswerIsCorrectByName() {
        DailyGamesScreenshot dailyGamesScreenshot1 = new DailyGamesScreenshot();
        dailyGamesScreenshot1.setName("correct answer");
        dailyGamesScreenshot1.setAlternativeNames(Arrays.asList("alternative name"));
        DailyGamesScreenshot dailyGamesScreenshot2 = new DailyGamesScreenshot();
        dailyGamesScreenshot2.setName("correct answer");
        dailyGamesScreenshot2.setAlternativeNames(Arrays.asList("alternative name again"));

        List<DailyGamesScreenshot> dailyGamesScreenshots = Arrays.asList(dailyGamesScreenshot1, dailyGamesScreenshot2);
        when(dailyGamesScreenshotsRepository.findAll()).thenReturn(dailyGamesScreenshots);

        Boolean result = dailyGamesScreenshotService.checkDailyGame(0, "correct answer");

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenAnswerIsCorrectByAlternativeNames() {
        DailyGamesScreenshot dailyGamesScreenshot1 = new DailyGamesScreenshot();
        dailyGamesScreenshot1.setName("wrong answer");
        dailyGamesScreenshot1.setAlternativeNames(Arrays.asList("correct answer"));
        DailyGamesScreenshot dailyGamesScreenshot2 = new DailyGamesScreenshot();
        dailyGamesScreenshot2.setName("wrong answer");
        dailyGamesScreenshot2.setAlternativeNames(Arrays.asList("wrong answer"));

        List<DailyGamesScreenshot> dailyGamesScreenshots = Arrays.asList(dailyGamesScreenshot1, dailyGamesScreenshot2);
        when(dailyGamesScreenshotsRepository.findAll()).thenReturn(dailyGamesScreenshots);

        Boolean result = dailyGamesScreenshotService.checkDailyGame(0, "correct answer");

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenAnswerIsIncorrect() {
        DailyGamesScreenshot dailyGamesScreenshot1 = new DailyGamesScreenshot();
        dailyGamesScreenshot1.setName("correct answer");
        dailyGamesScreenshot1.setAlternativeNames(Arrays.asList("alternative name"));
        DailyGamesScreenshot dailyGamesScreenshot2 = new DailyGamesScreenshot();
        dailyGamesScreenshot2.setName("wrong answer");
        dailyGamesScreenshot1.setAlternativeNames(Arrays.asList("different alternative name"));
        List<DailyGamesScreenshot> dailyGamesScreenshots = Arrays.asList(dailyGamesScreenshot1, dailyGamesScreenshot2);
        when(dailyGamesScreenshotsRepository.findAll()).thenReturn(dailyGamesScreenshots);

        Boolean result = dailyGamesScreenshotService.checkDailyGame(0, "wrong answer");

        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenNoDailyGamesToCheck() {
        when(dailyGamesScreenshotsRepository.findAll()).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> dailyGamesScreenshotService.checkDailyGame(0, "any answer"));
    }

    @Test
    void shouldUpdateDailyGamesWhenGamesAndScreenshotsExist() {
        when(gamesService.getNbrGames()).thenReturn(10L);
        when(screenshotsService.count()).thenReturn(10L);
        Games game = new Games();
        game.setName("Game");
        game.setScreenshots(Arrays.asList(1, 2, 3));
        when(gamesService.findNRandomGames(1)).thenReturn(Arrays.asList(game));
        Screenshots screenshot = new Screenshots();
        screenshot.setImageId("imageId");
        when(screenshotsService.getScreenshotsById(anyInt())).thenReturn(screenshot);
        when(screenshotsService.generateUrl(anyString())).thenReturn("url");
        when(alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(anyList())).thenReturn(Arrays.asList("Alternative Name"));

        dailyGamesScreenshotService.updateDailyGames();

        verify(dailyGamesScreenshotsRepository, times(10)).save(any(DailyGamesScreenshot.class));
    }

    @Test
    void shouldNotUpdateDailyGamesWhenNoGamesExist() {
        when(gamesService.getNbrGames()).thenReturn(0L);

        dailyGamesScreenshotService.updateDailyGames();

        verify(dailyGamesScreenshotsRepository, never()).save(any(DailyGamesScreenshot.class));
    }

    @Test
    void shouldNotUpdateDailyGamesWhenNoScreenshotsExist() {
        when(gamesService.getNbrGames()).thenReturn(10L);
        when(screenshotsService.count()).thenReturn(0L);

        dailyGamesScreenshotService.updateDailyGames();

        verify(dailyGamesScreenshotsRepository, never()).save(any(DailyGamesScreenshot.class));
    }

    @Test
    void shouldSkipInvalidGamesWhenUpdatingDailyGames() {
        when(gamesService.getNbrGames()).thenReturn(10L);
        when(screenshotsService.count()).thenReturn(10L);
        Games game1 = new Games();
        game1.setName("");
        game1.setScreenshots(Arrays.asList(1, 2, 3));
        Games game2 = new Games();
        game2.setName("Game");
        game2.setScreenshots(Arrays.asList(1, 2, 3));
        when(gamesService.findNRandomGames(1)).thenReturn(Arrays.asList(game1), Arrays.asList(game2));
        Screenshots screenshot = new Screenshots();
        screenshot.setImageId("imageId");
        when(screenshotsService.getScreenshotsById(anyInt())).thenReturn(screenshot);
        when(screenshotsService.generateUrl(anyString())).thenReturn("url");
        when(alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(anyList())).thenReturn(Arrays.asList("Alternative Name"));

        dailyGamesScreenshotService.updateDailyGames();

        verify(dailyGamesScreenshotsRepository, times(10)).save(any(DailyGamesScreenshot.class));
    }
}