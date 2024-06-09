package com.zytoune.dailygame.job;

import com.zytoune.dailygame.service.DailyGameService;
import com.zytoune.dailygame.service.DailyGamesScreenshotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DailygameDevJobTest {

    @Mock
    private DailyGameService dailyGameService;

    @Mock
    private DailyGamesScreenshotService dailyGamesScreenshotService;

    @InjectMocks
    private DailygameDevJob dailygameDevJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldUpdateDailyGamesDev() {
        dailygameDevJob.updateDailyGamesDev();

        verify(dailyGameService, times(1)).updateDailyGames();
        verify(dailyGamesScreenshotService, times(1)).updateDailyGames();
    }
}