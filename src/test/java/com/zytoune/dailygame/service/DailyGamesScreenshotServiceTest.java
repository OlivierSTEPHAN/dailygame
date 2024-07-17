package com.zytoune.dailygame.service;

import com.zytoune.dailygame.dto.DailyGamesScreenshotsDTO;
import com.zytoune.dailygame.dto.DailyGamesScreenshotsUrlDTO;
import com.zytoune.dailygame.entity.games.DailyGameScreenshotArchive;
import com.zytoune.dailygame.entity.games.DailyGamesScreenshot;
import com.zytoune.dailygame.entity.games.Games;
import com.zytoune.dailygame.entity.games.Screenshots;
import com.zytoune.dailygame.repository.games.DailyGameScreenshotArchiveRepository;
import com.zytoune.dailygame.repository.games.DailyGamesScreenshotsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DailyGamesScreenshotServiceTest {
    @Mock
    private DailyGamesScreenshotsRepository dailyGamesScreenshotsRepository;
    @Mock
    private DailyGameScreenshotArchiveRepository dailyGameScreenshotArchiveRepository;

    @Mock
    private GamesService gamesService;

    @Mock
    private ScreenshotsService screenshotsService;

    @Mock
    private AlternativeNamesService alternativeNamesService;

    @Mock
    private FranchisesService franchisesService;

    @InjectMocks
    private DailyGamesScreenshotService dailyGamesScreenshotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDailyGamesReturnsCorrectSortedDataWhenGamesExist() {
        List<DailyGamesScreenshot> mockDailyGamesScreenshots = List.of(
                new DailyGamesScreenshot(1, "Game 1", List.of("AltName1"), "url1", List.of("Franchise1"), new ArrayList<>()),
                new DailyGamesScreenshot(2, "Game 2", List.of("AltName2"), "url2", List.of("Franchise2"), new ArrayList<>())
        );
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(mockDailyGamesScreenshots);

        DailyGamesScreenshotsDTO result = dailyGamesScreenshotService.getDailyGames();

        assertEquals(2, result.getName().size());
        assertEquals(result.getName().get(0), "Game 1");
        assertTrue(result.getName().contains("Game 1"));
        assertTrue(result.getUrl().contains("url1"));
        assertTrue(result.getName().contains("Game 2"));
        assertTrue(result.getUrl().contains("url2"));
    }

    @Test
    void getDailyGamesThrowsExceptionWhenNoGamesFound() {
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> dailyGamesScreenshotService.getDailyGames());
    }

    @Test
    void shouldReturnDailyGamesWhenExist() {
        List<DailyGamesScreenshot> dailyGamesScreenshots = Arrays.asList(new DailyGamesScreenshot(), new DailyGamesScreenshot());
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(dailyGamesScreenshots);

        DailyGamesScreenshotsUrlDTO result = dailyGamesScreenshotService.getDailyGamesUrl();

        assertEquals(2, result.getScreenshots().size());
    }

    @Test
    void shouldThrowExceptionWhenNoDailyGames() {
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> dailyGamesScreenshotService.getDailyGamesUrl());
    }

    @Test
    void shouldReturnTrueWhenAnswerIsCorrectByName() {
        DailyGamesScreenshot dailyGamesScreenshot1 = new DailyGamesScreenshot();
        dailyGamesScreenshot1.setName("Pokémon Legends: Arceus");
        dailyGamesScreenshot1.setAlternativeNames(List.of("alternative name"));
        dailyGamesScreenshot1.setFranchises(List.of("franchise"));
        DailyGamesScreenshot dailyGamesScreenshot2 = new DailyGamesScreenshot();
        dailyGamesScreenshot2.setName("Alone in the dark");
        dailyGamesScreenshot2.setAlternativeNames(List.of("alternative name again"));

        List<DailyGamesScreenshot> dailyGamesScreenshots = Arrays.asList(dailyGamesScreenshot1, dailyGamesScreenshot2);
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(dailyGamesScreenshots);

        Boolean result = dailyGamesScreenshotService.checkDailyGame(0, "Pokémon Legends: Arceus");

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenAnswerIsCorrectByFranchise() {
        DailyGamesScreenshot dailyGamesScreenshot1 = new DailyGamesScreenshot();
        dailyGamesScreenshot1.setName("FIFA Soccer 07");
        dailyGamesScreenshot1.setAlternativeNames(List.of("FIFA Football 07"));
        dailyGamesScreenshot1.setFranchises(List.of("FIFA"));
        DailyGamesScreenshot dailyGamesScreenshot2 = new DailyGamesScreenshot();
        dailyGamesScreenshot2.setName("Alone in the dark");
        dailyGamesScreenshot2.setAlternativeNames(List.of("alternative name again"));

        List<DailyGamesScreenshot> dailyGamesScreenshots = Arrays.asList(dailyGamesScreenshot1, dailyGamesScreenshot2);
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(dailyGamesScreenshots);

        Boolean result = dailyGamesScreenshotService.checkDailyGame(0, "FIFA Soccer 08");

        assertTrue(result);
    }

    @Test
    void shouldReturnTrueWhenAnswerIsCorrectByAlternativeNames() {
        DailyGamesScreenshot dailyGamesScreenshot1 = new DailyGamesScreenshot();
        dailyGamesScreenshot1.setName("wrong answer");
        dailyGamesScreenshot1.setAlternativeNames(List.of("correct answer"));
        DailyGamesScreenshot dailyGamesScreenshot2 = new DailyGamesScreenshot();
        dailyGamesScreenshot2.setName("wrong answer");
        dailyGamesScreenshot2.setAlternativeNames(List.of("wrong answer"));

        List<DailyGamesScreenshot> dailyGamesScreenshots = Arrays.asList(dailyGamesScreenshot1, dailyGamesScreenshot2);
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(dailyGamesScreenshots);

        Boolean result = dailyGamesScreenshotService.checkDailyGame(0, "correct answer");

        assertTrue(result);
    }

    @Test
    void shouldReturnFalse() {
        DailyGamesScreenshot dailyGamesScreenshot1 = new DailyGamesScreenshot();
        dailyGamesScreenshot1.setName("Brothers in Arms: Hell's Highway");
        dailyGamesScreenshot1.setAlternativeNames(List.of("브라더스 인 암즈 헬즈 하이웨이"));
        dailyGamesScreenshot1.setFranchises(List.of());

        List<DailyGamesScreenshot> dailyGamesScreenshots = List.of(dailyGamesScreenshot1);
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(dailyGamesScreenshots);

        Boolean result = dailyGamesScreenshotService.checkDailyGame(0, "Fallout: Brotherhood of Steel 2");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenAnswerIsIncorrect() {
        DailyGamesScreenshot dailyGamesScreenshot1 = new DailyGamesScreenshot();
        dailyGamesScreenshot1.setName("correct answer");
        dailyGamesScreenshot1.setAlternativeNames(List.of("alternative name"));
        dailyGamesScreenshot1.setFranchises(List.of("franchise"));
        DailyGamesScreenshot dailyGamesScreenshot2 = new DailyGamesScreenshot();
        dailyGamesScreenshot2.setName("wrong answer");
        dailyGamesScreenshot1.setAlternativeNames(List.of("different alternative name"));
        dailyGamesScreenshot1.setFranchises(List.of());
        List<DailyGamesScreenshot> dailyGamesScreenshots = Arrays.asList(dailyGamesScreenshot1, dailyGamesScreenshot2);
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(dailyGamesScreenshots);

        Boolean result = dailyGamesScreenshotService.checkDailyGame(0, "wrong answer");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenAnswerIsEmpty() {
        DailyGamesScreenshot dailyGamesScreenshot1 = new DailyGamesScreenshot();
        dailyGamesScreenshot1.setName("correct answer");
        dailyGamesScreenshot1.setAlternativeNames(List.of("alternative name"));
        dailyGamesScreenshot1.setFranchises(List.of("franchise"));
        DailyGamesScreenshot dailyGamesScreenshot2 = new DailyGamesScreenshot();
        dailyGamesScreenshot2.setName("wrong answer");
        dailyGamesScreenshot1.setAlternativeNames(List.of("different alternative name"));
        dailyGamesScreenshot1.setFranchises(List.of(""));
        List<DailyGamesScreenshot> dailyGamesScreenshots = Arrays.asList(dailyGamesScreenshot1, dailyGamesScreenshot2);
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(dailyGamesScreenshots);

        Boolean result = dailyGamesScreenshotService.checkDailyGame(0, "");

        assertFalse(result);
    }

    @Test
    void shouldThrowExceptionWhenNoDailyGamesToCheck() {
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> dailyGamesScreenshotService.checkDailyGame(0, "any answer"));
    }

    @Test
    void shouldUpdateDailyGamesWhenGamesAndScreenshotsExist() {
        when(gamesService.getNbrGames()).thenReturn(10L);
        when(screenshotsService.count()).thenReturn(10L);
        Games game = new Games();
        game.setName("Game");
        game.setScreenshots(Arrays.asList(1, 2, 3));
        when(gamesService.findNRandomGames(anyInt(), anyInt())).thenReturn(List.of(game));
        Screenshots screenshot = new Screenshots();
        screenshot.setImageId("imageId");
        when(screenshotsService.getScreenshotsById(anyInt())).thenReturn(screenshot);
        when(screenshotsService.generateUrl(anyString(), any())).thenReturn("url");
        when(alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(anyList())).thenReturn(List.of("Alternative Name"));

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
        when(gamesService.findNRandomGames(anyInt(), anyInt())).thenReturn(List.of(game1), List.of(game2));
        Screenshots screenshot = new Screenshots();
        screenshot.setImageId("imageId");
        when(screenshotsService.getScreenshotsById(anyInt())).thenReturn(screenshot);
        when(screenshotsService.generateUrl(anyString(), any())).thenReturn("url");
        when(alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(anyList())).thenReturn(List.of("Alternative Name"));

        dailyGamesScreenshotService.updateDailyGames();

        verify(dailyGamesScreenshotsRepository, times(10)).save(any(DailyGamesScreenshot.class));
    }

    @Test
    void addingScoreToAllDailyGamesUpdatesScoresSuccessfully() {
        List<DailyGamesScreenshot> mockDailyGamesScreenshots = List.of(
                new DailyGamesScreenshot(1, "Game 1", List.of("AltName1"), "url1", List.of("Franchise1"), new ArrayList<>(List.of(10))),
                new DailyGamesScreenshot(2, "Game 2", List.of("AltName2"), "url2", List.of("Franchise2"), new ArrayList<>(List.of(20)))
        );
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(mockDailyGamesScreenshots);

        dailyGamesScreenshotService.addScore(List.of(30, 40));

        assertEquals(2, mockDailyGamesScreenshots.get(0).getScores().size());
        assertTrue(mockDailyGamesScreenshots.get(0).getScores().contains(30));
        assertEquals(2, mockDailyGamesScreenshots.get(1).getScores().size());
        assertTrue(mockDailyGamesScreenshots.get(1).getScores().contains(40));
        verify(dailyGamesScreenshotsRepository, times(2)).save(any(DailyGamesScreenshot.class));
    }

    @Test
    void addingScoreFailsWhenScoresListSizeDoesNotMatchDailyGamesSize() {
        List<DailyGamesScreenshot> mockDailyGamesScreenshots = List.of(new DailyGamesScreenshot());
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(mockDailyGamesScreenshots);

        Exception exception = assertThrows(RuntimeException.class, () -> dailyGamesScreenshotService.addScore(List.of(10, 20)));

        assertEquals("Daily games and scores length don't match, no score will be added", exception.getMessage());
    }

    @Test
    void addingScoreFailsWhenNoDailyGamesFound() {
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> dailyGamesScreenshotService.addScore(List.of(10)));

        assertEquals("No daily games found", exception.getMessage());
    }

    @Test
    void getScoreReturnsAverageScoresForAllDailyGames() {
        List<DailyGamesScreenshot> mockDailyGamesScreenshots = List.of(
                new DailyGamesScreenshot(1 ,"Game 1", List.of("AltName1"), "url1", List.of("Franchise1"), new ArrayList<>(List.of(10, 20))),
                new DailyGamesScreenshot(2, "Game 2", List.of("AltName2"), "url2", List.of("Franchise2"), new ArrayList<>(List.of(30, 40, 50)))
        );
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(mockDailyGamesScreenshots);

        List<Integer> scores = dailyGamesScreenshotService.getScore();

        assertEquals(List.of(15, 40), scores);
    }

    @Test
    void getScoreReturnsZerosWhenNoScoresPresentInDailyGames() {
        List<DailyGamesScreenshot> mockDailyGamesScreenshots = List.of(
                new DailyGamesScreenshot(1, "Game 1", new ArrayList<>(), "url1", List.of("AltName1"), List.of()),
                new DailyGamesScreenshot(2, "Game 2", new ArrayList<>(), "url2", List.of("AltName2"), List.of())
        );
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(mockDailyGamesScreenshots);

        List<Integer> scores = dailyGamesScreenshotService.getScore();

        assertEquals(List.of(0, 0), scores);
    }

    @Test
    void getScoreFailsWhenNoDailyGamesFound() {
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(List.of());

        Exception exception = assertThrows(RuntimeException.class, () -> dailyGamesScreenshotService.getScore());

        assertEquals("No daily games found", exception.getMessage());
    }

    @Test
    void updatingDailyGamesWithNoGamesOrScreenshotsDoesNotProceed() {
        when(gamesService.getNbrGames()).thenReturn(0L);
        when(screenshotsService.count()).thenReturn(0L);

        dailyGamesScreenshotService.updateDailyGames();

        verify(dailyGamesScreenshotsRepository, never()).findAllByOrderByIdAsc();
        verify(dailyGameScreenshotArchiveRepository, never()).save(any(DailyGameScreenshotArchive.class));
        verify(dailyGamesScreenshotsRepository, never()).deleteAll();
    }

    @Test
    void updatingDailyGamesArchivesExistingGames() {
        when(gamesService.getNbrGames()).thenReturn(1L);
        Games game = new Games();
        game.setName("Valid Game");
        game.setScreenshots(List.of(1));
        when(screenshotsService.getScreenshotsById(anyInt())).thenReturn(new Screenshots());
        when(gamesService.findNRandomGames(anyInt(), anyInt())).thenReturn(List.of(game));
        when(screenshotsService.count()).thenReturn(1L);
        when(dailyGamesScreenshotsRepository.count()).thenReturn(1L);
        List<DailyGamesScreenshot> existingScreenshots = List.of(new DailyGamesScreenshot());
        when(dailyGamesScreenshotsRepository.findAllByOrderByIdAsc()).thenReturn(existingScreenshots);

        dailyGamesScreenshotService.updateDailyGames();

        verify(dailyGameScreenshotArchiveRepository, times(1)).save(any(DailyGameScreenshotArchive.class));
        verify(dailyGamesScreenshotsRepository, times(1)).deleteAll();
    }

    @Test
    void updatingDailyGamesSavesNewGamesWhenValid() {
        when(gamesService.getNbrGames()).thenReturn(10L);
        when(screenshotsService.count()).thenReturn(10L);
        when(dailyGamesScreenshotsRepository.count()).thenReturn(0L); // No existing daily games screenshots
        Games game = new Games();
        game.setName("Valid Game");
        game.setScreenshots(List.of(1));
        when(gamesService.findNRandomGames(1, 75)).thenReturn(List.of(game));
        when(screenshotsService.getScreenshotsById(anyInt())).thenReturn(new Screenshots());
        when(screenshotsService.generateUrl(anyString(), any())).thenReturn("validUrl");

        dailyGamesScreenshotService.updateDailyGames();

        verify(dailyGamesScreenshotsRepository, atLeastOnce()).save(any(DailyGamesScreenshot.class));
    }

    @Test
    void updatingDailyGamesRetriesWhenInvalidGameFound() {
        when(gamesService.getNbrGames()).thenReturn(10L);
        when(screenshotsService.count()).thenReturn(10L);
        Games invalidGame = new Games();
        invalidGame.setName("");
        invalidGame.setScreenshots(List.of());
        Games validGame = new Games();
        validGame.setName("Valid Game");
        validGame.setScreenshots(List.of(1));
        when(gamesService.findNRandomGames(1, 75))
                .thenReturn(List.of(invalidGame)) // First call returns invalid game
                .thenReturn(List.of(validGame)); // Second call returns valid game
        when(screenshotsService.getScreenshotsById(anyInt())).thenReturn(new Screenshots());
        when(screenshotsService.generateUrl(anyString(), any())).thenReturn("validUrl");

        dailyGamesScreenshotService.updateDailyGames();

        verify(dailyGamesScreenshotsRepository, atLeastOnce()).save(any(DailyGamesScreenshot.class));
    }

}