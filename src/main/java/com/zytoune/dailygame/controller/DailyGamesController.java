package com.zytoune.dailygame.controller;

import com.zytoune.dailygame.dto.*;
import com.zytoune.dailygame.service.DailyGameService;
import com.zytoune.dailygame.service.DailyGamesScreenshotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RequestMapping("/daily-games")
@RestController
public class DailyGamesController {

    private DailyGamesScreenshotService dailyGamesScreenshotsService;
    private DailyGameService dailyGameService;


    @GetMapping("/screenshots/url")
    public ResponseEntity<DailyGamesScreenshotsUrlDTO> getDailyGamesScreenshotsUrl() {
        return ResponseEntity.ok(dailyGamesScreenshotsService.getDailyGamesUrl());
    }

    @GetMapping("/screenshots")
    public ResponseEntity<DailyGamesScreenshotsDTO> getDailyGamesScreenshots() {
        return ResponseEntity.ok(dailyGamesScreenshotsService.getDailyGames());
    }

    @PostMapping("/screenshots")
    public ResponseEntity<Boolean> isDailyGameScreenshotOk(@RequestBody DailyGameScreenshotAnswerDTO dailyGameAnswer) {
        log.info("Checking daily games {} - {}", dailyGameAnswer.getIndex(), dailyGameAnswer.getAnswer());
        return ResponseEntity.ok(dailyGamesScreenshotsService.checkDailyGame(dailyGameAnswer.getIndex(), dailyGameAnswer.getAnswer().trim()));
    }

    @PostMapping("/screenshots/score")
    public ResponseEntity<Boolean> isDailyGameScreenshotScoreOk(@RequestBody DailyGamesScreenshotScoreDTO dailyGamesScore) {
        log.info("Daily games screenshot score {}", dailyGamesScore.getDailyGamesScore().stream().mapToInt(Integer::intValue).sum());
        dailyGamesScreenshotsService.addScore(dailyGamesScore.getDailyGamesScore());
        return ResponseEntity.ok(true);
    }

    @GetMapping("/screenshots/score")
    public ResponseEntity<List<Integer>> getDailyGamesScreenshotScore() {
        return ResponseEntity.ok(dailyGamesScreenshotsService.getScore());
    }

    @GetMapping("/characteristics")
    public ResponseEntity<DailyGameDTO> getDailyGame() {
        log.info("Getting daily game");
        return ResponseEntity.ok(dailyGameService.getDailyGame());
    }

    @PostMapping("/characteristics")
    public ResponseEntity<DailyGameDTO> isDailyGameOk(@RequestBody DailyGameAnswerDTO dailyGameAnswer) {
        log.info("Checking daily games {}", dailyGameAnswer.getAnswer());
        return ResponseEntity.ok(dailyGameService.checkAnswer(dailyGameAnswer.getAnswer().trim()));
    }

    @PostMapping("/characteristics/score")
    public ResponseEntity<Boolean> isDailyGameScoreOk(@RequestParam Integer dailyGameScore) {
        log.info("Daily games score {}", dailyGameScore);
        dailyGameService.addScore(dailyGameScore);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/characteristics/score")
    public ResponseEntity<Integer> getDailyGameScore() {
        return ResponseEntity.ok(dailyGameService.getScore());
    }
}
