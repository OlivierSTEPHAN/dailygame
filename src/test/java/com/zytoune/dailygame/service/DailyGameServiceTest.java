package com.zytoune.dailygame.service;

import com.zytoune.dailygame.dto.DailyGameDTO;
import com.zytoune.dailygame.entity.games.DailyGame;
import com.zytoune.dailygame.entity.games.DailyGameArchive;
import com.zytoune.dailygame.entity.games.Games;
import com.zytoune.dailygame.exception.NoGameFoundException;
import com.zytoune.dailygame.repository.games.DailyGameArchiveRepository;
import com.zytoune.dailygame.repository.games.DailyGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class DailyGameServiceTest {
    private static final Logger log = LoggerFactory.getLogger(DailyGameServiceTest.class);
    @Mock
    private DailyGameRepository dailyGameRepository;
    @Mock
    private DailyGameArchiveRepository dailyGameArchiveRepository;
    @Mock
    private GamesService gamesService;
    @Mock
    private AlternativeNamesService alternativeNamesService;
    @Mock
    private GenresService genresService;
    @Mock
    private PlayerPerspectivesService playerPerspectivesService;
    @Mock
    private FranchisesService franchisesService;
    @Mock
    private CompaniesService companiesService;
    @Mock
    private PlatformsService platformsService;
    @Mock
    private GameModesService gameModesService;
    @Mock
    private GameEnginesService gameEnginesService;

    @Spy
    @InjectMocks
    private DailyGameService dailyGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(gameEnginesService.getGameEngineById(anyList())).thenReturn(Collections.singletonList("engineName"));

    }

    @Test
    void shouldReturnDailyGame() {
        DailyGame dailyGame = mock(DailyGame.class);
        when(dailyGameRepository.findAll()).thenReturn(Collections.singletonList(dailyGame));

        DailyGameDTO result = dailyGameService.getDailyGame();

        assertNotNull(result);
    }

    @Test
    void shouldThrowExceptionWhenNoDailyGameFound() {
        when(dailyGameRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> dailyGameService.getDailyGame());
    }

    @Test
    void shouldReturnDailyGameWhenAnswerIsCorrect() {
        Games game = mock(Games.class);
        when(game.getName()).thenReturn("gameName");
        when(gamesService.findGameByGameName(anyString())).thenReturn(game);

        DailyGame dailyGame = mock(DailyGame.class);
        when(dailyGame.getName()).thenReturn("gameName");
        when(dailyGameRepository.findAll()).thenReturn(Collections.singletonList(dailyGame));

        DailyGameDTO result = dailyGameService.checkAnswer("gameName");

        assertNotNull(result);
    }

    @Test
    void shouldThrowExceptionWhenGameNotFound() {
        when(gamesService.findGameByGameName(anyString())).thenReturn(null);

        assertThrows(NoGameFoundException.class, () -> dailyGameService.checkAnswer("gameName"));
    }

    @Test
    void shouldThrowExceptionWhenNoDailyGamesFound() {
        Games game = mock(Games.class);
        when(game.getName()).thenReturn("gameName");
        when(gamesService.findGameByGameName(anyString())).thenReturn(game);

        when(dailyGameRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(NoGameFoundException.class, () -> dailyGameService.checkAnswer("gameName"));
    }

    @Test
    void shouldReturnDailyGameDTOWhenAlternativeNameMatches() {
        Games game = mock(Games.class);
        when(game.getName()).thenReturn("gameName");
        when(game.getAlternativeNames()).thenReturn(Arrays.asList(1, 2));
        when(gamesService.findGameByGameName(anyString())).thenReturn(game);

        DailyGame dailyGame = mock(DailyGame.class);
        when(dailyGame.getName()).thenReturn("alternativeName");
        when(dailyGameRepository.findAll()).thenReturn(Collections.singletonList(dailyGame));

        when(alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(anyList())).thenReturn(Collections.singletonList("alternativeName"));

        DailyGameDTO result = dailyGameService.checkAnswer("gameName");

        assertNotNull(result);
    }

    @Test
    void updatingDailyGamesWithNoGamesDoesNotProceed() {
        when(gamesService.getNbrGames()).thenReturn(0L);

        dailyGameService.updateDailyGames();

        verify(dailyGameRepository, never()).findAll();
        verify(dailyGameArchiveRepository, never()).save(any(DailyGameArchive.class));
        verify(dailyGameRepository, never()).save(any(DailyGame.class));
    }

    @Test
    void updatingDailyGamesArchivesExistingDailyGame() {
        when(gamesService.getNbrGames()).thenReturn(1L);
        when(dailyGameRepository.count()).thenReturn(1L);
        List<DailyGame> existingDailyGames = List.of(new DailyGame());
        when(dailyGameRepository.findAll()).thenReturn(existingDailyGames);

        Games newGame = new Games();
        when(gamesService.findNRandomGames(1, 150)).thenReturn(List.of(newGame));
        when(dailyGameService.returnCompatibleGameOrElseNull(newGame)).thenReturn(new DailyGame());

        dailyGameService.updateDailyGames();

        verify(dailyGameArchiveRepository, times(1)).save(any(DailyGameArchive.class));
        verify(dailyGameRepository, times(1)).deleteAll();
    }

    @Test
    void updatingDailyGamesSavesNewDailyGameWhenFound() {
        when(gamesService.getNbrGames()).thenReturn(1L);
        when(dailyGameRepository.count()).thenReturn(0L); // No existing daily games

        Games game = new Games();
        when(gamesService.findNRandomGames(1, 150)).thenReturn(List.of(game));
        DailyGame compatibleGame = new DailyGame();
        when(dailyGameService.returnCompatibleGameOrElseNull(game)).thenReturn(compatibleGame);

        dailyGameService.updateDailyGames();

        verify(dailyGameRepository, times(1)).save(compatibleGame);
    }

    @Test
    void updatingDailyGamesRetriesFindingNewGameUntilCompatibleOneIsFound() {
        when(gamesService.getNbrGames()).thenReturn(1L);
        when(dailyGameRepository.count()).thenReturn(0L); // No existing daily games

        Games incompatibleGame = new Games();
        Games compatibleGame = new Games();
        when(gamesService.findNRandomGames(1, 150))
                .thenReturn(List.of(incompatibleGame)) // First call returns incompatible game
                .thenReturn(List.of(compatibleGame)); // Second call returns compatible game

        when(dailyGameService.returnCompatibleGameOrElseNull(incompatibleGame)).thenReturn(null); // Incompatible
        when(dailyGameService.returnCompatibleGameOrElseNull(compatibleGame)).thenReturn(new DailyGame()); // Compatible

        dailyGameService.updateDailyGames();

        verify(gamesService, times(2)).findNRandomGames(1, 150); // Ensure method was called twice
        verify(dailyGameRepository, times(1)).save(any(DailyGame.class)); // Ensure a daily game was saved
    }


    @Test
    void shouldReturnDailyGameDTOWhenGameIsComplete() {
        Games game = mock(Games.class);
        when(game.getName()).thenReturn("gameName");
        when(game.getGenres()).thenReturn(Arrays.asList(1, 2));
        when(game.getPlayerPerspectives()).thenReturn(Arrays.asList(1, 2));
        when(game.getFranchises()).thenReturn(Arrays.asList(1, 2));
        when(game.getInvolvedCompanies()).thenReturn(Arrays.asList(1, 2));
        when(game.getFirstReleaseDate()).thenReturn(946684800); // 01-01-2000
        when(game.getPlatforms()).thenReturn(Arrays.asList(1, 2));
        when(game.getGameModes()).thenReturn(Arrays.asList(1, 2));
        when(game.getGameEngines()).thenReturn(Arrays.asList(1, 2));

        when(genresService.getGenresByGenreIds(anyList())).thenReturn(Arrays.asList("genre1", "genre2"));
        when(playerPerspectivesService.getPlayerPerspectivesByIds(anyList())).thenReturn(Arrays.asList("pov1", "pov2"));
        when(franchisesService.getFranchisesByIds(anyList())).thenReturn(Arrays.asList("franchise1", "franchise2"));
        when(companiesService.getCompaniesByIds(anyList())).thenReturn(Arrays.asList("company1", "company2"));
        when(platformsService.getPlatformsByIds(anyList())).thenReturn(Arrays.asList("platform1", "platform2"));
        when(gameModesService.getGameModesByIds(anyList())).thenReturn(Arrays.asList("gameMode1", "gameMode2"));
        when(gameEnginesService.getGameEngineById(anyList())).thenReturn(Arrays.asList("gameEngine1", "gameEngine2"));

        DailyGameDTO result = dailyGameService.mappingGameToDTO(game);

        assertNotNull(result);
        assertEquals("gameName", result.getName());
        assertEquals(Arrays.asList("genre1", "genre2"), result.getGenres());
        assertEquals(Arrays.asList("pov1", "pov2"), result.getPov());
        assertEquals(Arrays.asList("franchise1", "franchise2"), result.getFranchises());
        assertEquals(Arrays.asList("company1", "company2"), result.getCompaniesName());
        assertEquals(2000, result.getYear());
        assertEquals(Arrays.asList("platform1", "platform2"), result.getPlatforms());
        assertEquals(Arrays.asList("gameMode1", "gameMode2"), result.getGameModes());
        assertEquals(Arrays.asList("gameEngine1", "gameEngine2"), result.getGameEngines());
    }

    @Test
    void shouldReturnDailyGameDTOWithGameNameAsFranchiseWhenFranchisesAreEmpty() {
        Games game = mock(Games.class);
        when(game.getName()).thenReturn("gameName");
        when(game.getGenres()).thenReturn(Arrays.asList(1, 2));
        when(game.getPlayerPerspectives()).thenReturn(Arrays.asList(1, 2));
        when(game.getFranchises()).thenReturn(Collections.emptyList());
        when(game.getInvolvedCompanies()).thenReturn(Arrays.asList(1, 2));
        when(game.getFirstReleaseDate()).thenReturn(946684800); // 01-01-2000
        when(game.getPlatforms()).thenReturn(Arrays.asList(1, 2));
        when(game.getGameModes()).thenReturn(Arrays.asList(1, 2));
        when(game.getGameEngines()).thenReturn(Arrays.asList(1, 2));

        when(genresService.getGenresByGenreIds(anyList())).thenReturn(Arrays.asList("genre1", "genre2"));
        when(playerPerspectivesService.getPlayerPerspectivesByIds(anyList())).thenReturn(Arrays.asList("pov1", "pov2"));
        when(companiesService.getCompaniesByIds(anyList())).thenReturn(Arrays.asList("company1", "company2"));
        when(platformsService.getPlatformsByIds(anyList())).thenReturn(Arrays.asList("platform1", "platform2"));
        when(gameModesService.getGameModesByIds(anyList())).thenReturn(Arrays.asList("gameMode1", "gameMode2"));
        when(gameEnginesService.getGameEngineById(anyList())).thenReturn(Arrays.asList("gameEngine1", "gameEngine2"));

        DailyGameDTO result = dailyGameService.mappingGameToDTO(game);

        assertNotNull(result);
        assertEquals("gameName", result.getName());
        assertEquals(Arrays.asList("genre1", "genre2"), result.getGenres());
        assertEquals(Arrays.asList("pov1", "pov2"), result.getPov());
        assertEquals(List.of("gameName"), result.getFranchises());
        assertEquals(Arrays.asList("company1", "company2"), result.getCompaniesName());
        assertEquals(2000, result.getYear());
        assertEquals(Arrays.asList("platform1", "platform2"), result.getPlatforms());
        assertEquals(Arrays.asList("gameMode1", "gameMode2"), result.getGameModes());
        assertEquals(Arrays.asList("gameEngine1", "gameEngine2"), result.getGameEngines());
    }

    @Test
    void shouldReturnCompatibleGameWhenGameIsComplete() {
        Games game = mock(Games.class);
        when(game.getName()).thenReturn("gameName");
        when(game.getAlternativeNames()).thenReturn(Arrays.asList(1,2));
        when(game.getGenres()).thenReturn(Arrays.asList(1, 2));
        when(game.getPlayerPerspectives()).thenReturn(Arrays.asList(1, 2));
        when(game.getFranchises()).thenReturn(Arrays.asList(1, 2));
        when(game.getInvolvedCompanies()).thenReturn(Arrays.asList(1, 2));
        when(game.getFirstReleaseDate()).thenReturn(946684800); // 01-01-2000
        when(game.getPlatforms()).thenReturn(Arrays.asList(1, 2));
        when(game.getGameModes()).thenReturn(Arrays.asList(1, 2));
        when(game.getGameEngines()).thenReturn(Arrays.asList(1, 2));

        when(alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(anyList())).thenReturn(Arrays.asList("alternativeName1", "alternativeName2"));
        when(genresService.getGenresByGenreIds(anyList())).thenReturn(Arrays.asList("genre1", "genre2"));
        when(playerPerspectivesService.getPlayerPerspectivesByIds(anyList())).thenReturn(Arrays.asList("pov1", "pov2"));
        when(franchisesService.getFranchisesByIds(anyList())).thenReturn(List.of());
        when(companiesService.getCompaniesByIds(anyList())).thenReturn(Arrays.asList("company1", "company2"));
        when(platformsService.getPlatformsByIds(anyList())).thenReturn(Arrays.asList("platform1", "platform2"));
        when(gameModesService.getGameModesByIds(anyList())).thenReturn(Arrays.asList("gameMode1", "gameMode2"));
        when(gameEnginesService.getGameEngineById(anyList())).thenReturn(Arrays.asList("gameEngine1", "gameEngine2"));

        DailyGame result = dailyGameService.returnCompatibleGameOrElseNull(game);

        assertNotNull(result);
        assertEquals("gameName", result.getName());
        assertEquals("gameName", result.getFranchises().get(0));
    }

    @Test
    void shouldReturnNullWhenGameIsIncomplete() {
        Games game = mock(Games.class);
        when(game.getName()).thenReturn("gameName");
        when(game.getAlternativeNames()).thenReturn(Arrays.asList(1,2));
        when(game.getGenres()).thenReturn(Arrays.asList(1, 2));
        when(game.getPlayerPerspectives()).thenReturn(Arrays.asList(1, 2));
        when(game.getFranchises()).thenReturn(Arrays.asList(1, 2));
        when(game.getInvolvedCompanies()).thenReturn(Arrays.asList(1, 2));
        when(game.getFirstReleaseDate()).thenReturn(946684800); // 01-01-2000
        when(game.getPlatforms()).thenReturn(Arrays.asList(1, 2));
        when(game.getGameEngines()).thenReturn(Arrays.asList(1, 2));

        when(alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(anyList())).thenReturn(Arrays.asList("alternativeName1", "alternativeName2"));
        when(genresService.getGenresByGenreIds(anyList())).thenReturn(Arrays.asList("genre1", "genre2"));
        when(playerPerspectivesService.getPlayerPerspectivesByIds(anyList())).thenReturn(Arrays.asList("pov1", "pov2"));
        when(franchisesService.getFranchisesByIds(anyList())).thenReturn(Arrays.asList("franchise1", "franchise2"));
        when(companiesService.getCompaniesByIds(anyList())).thenReturn(Arrays.asList("company1", "company2"));
        when(gameModesService.getGameModesByIds(anyList())).thenReturn(Arrays.asList("gameMode1", "gameMode2"));
        when(gameEnginesService.getGameEngineById(anyList())).thenReturn(Arrays.asList("gameEngine1", "gameEngine2"));

        DailyGame result = dailyGameService.returnCompatibleGameOrElseNull(game);

        assertNull(result);
    }

    @Test
    void addingScoreToExistingDailyGameIncreasesScoreList() {
        List<DailyGame> existingDailyGames = List.of(new DailyGame());
        existingDailyGames.get(0).setScores(new ArrayList<>(List.of(10, 20)));
        when(dailyGameRepository.findAll()).thenReturn(existingDailyGames);

        dailyGameService.addScore(30);

        assertEquals(3, existingDailyGames.get(0).getScores().size());
        assertTrue(existingDailyGames.get(0).getScores().contains(30));
        verify(dailyGameRepository, times(1)).save(any(DailyGame.class));
    }

    @Test
    void addingScoreThrowsExceptionWhenNoDailyGamesFound() {
        when(dailyGameRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> dailyGameService.addScore(10));
    }

    @Test
    void getScoreReturnsAverageScoreForDailyGame() {
        List<DailyGame> existingDailyGames = List.of(new DailyGame());
        existingDailyGames.get(0).setScores(new ArrayList<>(List.of(10, 20, 30)));
        when(dailyGameRepository.findAll()).thenReturn(existingDailyGames);

        int averageScore = dailyGameService.getScore();

        assertEquals(20, averageScore);
    }

    @Test
    void getScoreReturnsZeroWhenNoScoresPresent() {
        List<DailyGame> existingDailyGames = List.of(new DailyGame());
        existingDailyGames.get(0).setScores(new ArrayList<>());
        when(dailyGameRepository.findAll()).thenReturn(existingDailyGames);

        int averageScore = dailyGameService.getScore();

        assertEquals(0, averageScore);
    }

    @Test
    void getScoreThrowsExceptionWhenNoDailyGamesFound() {
        when(dailyGameRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> dailyGameService.getScore());
    }
}