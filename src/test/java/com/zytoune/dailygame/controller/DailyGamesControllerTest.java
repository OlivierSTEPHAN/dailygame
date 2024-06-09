package com.zytoune.dailygame.controller;

import com.zytoune.dailygame.dto.DailyGameAnswerDTO;
import com.zytoune.dailygame.dto.DailyGameDTO;
import com.zytoune.dailygame.dto.DailyGameScreenshotAnswerDTO;
import com.zytoune.dailygame.dto.DailyGamesScreenshotsDTO;
import com.zytoune.dailygame.service.DailyGameService;
import com.zytoune.dailygame.service.DailyGamesScreenshotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
}