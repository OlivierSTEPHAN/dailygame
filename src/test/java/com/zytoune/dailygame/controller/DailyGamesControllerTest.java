package com.zytoune.dailygame.controller;

import com.zytoune.dailygame.dto.*;
import com.zytoune.dailygame.service.DailyGameService;
import com.zytoune.dailygame.service.DailyGamesScreenshotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DailyGamesControllerTest {

    @Mock
    private DailyGamesScreenshotService dailyGamesScreenshotsService;

    @Mock
    private DailyGameService dailyGameService;

    @InjectMocks
    private DailyGamesController dailyGamesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnDailyGamesScreenshots() {
        DailyGamesScreenshotsDTO dailyGamesScreenshotsDTO = new DailyGamesScreenshotsDTO();
        when(dailyGamesScreenshotsService.getDailyGames()).thenReturn(dailyGamesScreenshotsDTO);

        ResponseEntity<DailyGamesScreenshotsDTO> responseEntity = dailyGamesController.getDailyGamesScreenshots();

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(dailyGamesScreenshotsDTO, responseEntity.getBody());
    }

    @Test
    void shouldReturnDailyGamesScreenshotsUrl() {
        DailyGamesScreenshotsUrlDTO dailyGamesScreenshotsUrlDTO = new DailyGamesScreenshotsUrlDTO();
        when(dailyGamesScreenshotsService.getDailyGamesUrl()).thenReturn(dailyGamesScreenshotsUrlDTO);

        ResponseEntity<DailyGamesScreenshotsUrlDTO> responseEntity = dailyGamesController.getDailyGamesScreenshotsUrl();

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(dailyGamesScreenshotsUrlDTO, responseEntity.getBody());
    }

    @Test
    void shouldCheckDailyGameScreenshot() {
        DailyGameScreenshotAnswerDTO dailyGameAnswer = new DailyGameScreenshotAnswerDTO();
        dailyGameAnswer.setAnswer("answer");
        when(dailyGamesScreenshotsService.checkDailyGame(dailyGameAnswer.getIndex(), dailyGameAnswer.getAnswer().trim())).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = dailyGamesController.isDailyGameScreenshotOk(dailyGameAnswer);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(true, responseEntity.getBody());
    }

    @Test
    void shouldReturnDailyGame() {
        DailyGameDTO dailyGameDTO = new DailyGameDTO();
        when(dailyGameService.getDailyGame()).thenReturn(dailyGameDTO);

        ResponseEntity<DailyGameDTO> responseEntity = dailyGamesController.getDailyGame();

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(dailyGameDTO, responseEntity.getBody());
    }

    @Test
    void shouldCheckDailyGame() {
        DailyGameAnswerDTO dailyGameAnswer = new DailyGameAnswerDTO();
        dailyGameAnswer.setAnswer("answer");
        DailyGameDTO dailyGameDTO = new DailyGameDTO();
        when(dailyGameService.checkAnswer(dailyGameAnswer.getAnswer().trim())).thenReturn(dailyGameDTO);

        ResponseEntity<DailyGameDTO> responseEntity = dailyGamesController.isDailyGameOk(dailyGameAnswer);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(dailyGameDTO, responseEntity.getBody());
    }

    @Test
    void addingScreenshotScoreUpdatesSuccessfully() {
        List<Integer> scoresToAdd = List.of(10, 20, 30);
        DailyGamesScreenshotScoreDTO dailyGamesScore = new DailyGamesScreenshotScoreDTO();
        dailyGamesScore.setDailyGamesScore(scoresToAdd);

        ResponseEntity<Boolean> response = dailyGamesController.isDailyGameScreenshotScoreOk(dailyGamesScore);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(dailyGamesScreenshotsService, times(1)).addScore(scoresToAdd);
    }

    @Test
    void getScreenshotScoresReturnsCorrectScores() {
        List<Integer> expectedScores = List.of(10, 20, 30);
        when(dailyGamesScreenshotsService.getScore()).thenReturn(expectedScores);

        ResponseEntity<List<Integer>> response = dailyGamesController.getDailyGamesScreenshotScore();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedScores, response.getBody());
    }

    @Test
    void addingScreenshotScoreWithEmptyListReturnsTrue() {
        List<Integer> scoresToAdd = new ArrayList<>();
        DailyGamesScreenshotScoreDTO dailyGamesScore = new DailyGamesScreenshotScoreDTO();
        dailyGamesScore.setDailyGamesScore(scoresToAdd);

        ResponseEntity<Boolean> response = dailyGamesController.isDailyGameScreenshotScoreOk(dailyGamesScore);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(dailyGamesScreenshotsService, times(1)).addScore(scoresToAdd);
    }

    @Test
    void getScreenshotScoresReturnsEmptyListWhenNoScores() {
        List<Integer> expectedScores = new ArrayList<>();
        when(dailyGamesScreenshotsService.getScore()).thenReturn(expectedScores);

        ResponseEntity<List<Integer>> response = dailyGamesController.getDailyGamesScreenshotScore();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void addingScoreUpdatesDailyGameScoreSuccessfully() {
        Integer scoreToAdd = 10;

        ResponseEntity<Boolean> response = dailyGamesController.isDailyGameScoreOk(scoreToAdd);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void addingScoreUpdatesDailyGameScoreFails() {
        int scoreToAdd = 10;

        doThrow(new RuntimeException("No daily games found")).when(dailyGameService).addScore(scoreToAdd);

        assertThrows(RuntimeException.class, () -> dailyGamesController.isDailyGameScoreOk(scoreToAdd));
    }

    @Test
    void getDailyGameScoreReturnsCorrectScore() {
        Integer expectedScore = 42;
        when(dailyGameService.getScore()).thenReturn(expectedScore);

        ResponseEntity<Integer> response = dailyGamesController.getDailyGameScore();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedScore, response.getBody());
    }
}