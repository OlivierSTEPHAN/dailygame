package com.zytoune.dailygame.job;

import com.zytoune.dailygame.service.DailyGameService;
import com.zytoune.dailygame.service.DailyGamesScreenshotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
@Profile("dev")
public class DailygameDevJob {

    private DailyGameService dailyGameService;
    private DailyGamesScreenshotService dailyGamesScreenshotService;

    @Scheduled(cron = "0 */3 * * * *")
    public void updateDailyGamesDev() {
        log.info("Updating daily game in dev mode");
        dailyGameService.updateDailyGames();
        dailyGamesScreenshotService.updateDailyGames();
    }
}
