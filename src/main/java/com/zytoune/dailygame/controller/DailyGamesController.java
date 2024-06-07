package com.zytoune.dailygame.controller;

import com.zytoune.dailygame.dto.DailyGameAnswerDTO;
import com.zytoune.dailygame.dto.DailyGameDTO;
import com.zytoune.dailygame.dto.DailyGameScreenshotAnswerDTO;
import com.zytoune.dailygame.dto.DailyGamesScreenshotsDTO;
import com.zytoune.dailygame.service.DailyGameService;
import com.zytoune.dailygame.service.DailyGamesScreenshotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RequestMapping("/daily-games")
@RestController
public class DailyGamesController {

    private DailyGamesScreenshotService dailyGamesScreenshotsService;
    private DailyGameService dailyGameService;


    @GetMapping("/screenshots")
    public ResponseEntity<DailyGamesScreenshotsDTO> getDailyGamesScreenshots() {
        log.info("Getting daily games screenshot");
        return ResponseEntity.ok(dailyGamesScreenshotsService.getDailyGames());
    }

    @PostMapping("/screenshots")
    public ResponseEntity<Boolean> isDailyGameScreenshotOk(@RequestBody DailyGameScreenshotAnswerDTO dailyGameAnswer) {
        log.info("Checking daily games {}", dailyGameAnswer);
        return ResponseEntity.ok(dailyGamesScreenshotsService.checkDailyGame(dailyGameAnswer.getIndex(), dailyGameAnswer.getAnswer().trim()));
    }

    @GetMapping("/characteristics")
    public ResponseEntity<DailyGameDTO> getDailyGame() {
        log.info("Getting daily game");
        return ResponseEntity.ok(dailyGameService.getDailyGame());
    }

    @PostMapping("/characteristics")
    public ResponseEntity<DailyGameDTO> isDailyGameOk(@RequestBody DailyGameAnswerDTO dailyGameAnswer) {
        log.info("Checking daily games {}", dailyGameAnswer);
        return ResponseEntity.ok(dailyGameService.checkAnswer(dailyGameAnswer.getAnswer().trim()));
    }
}
