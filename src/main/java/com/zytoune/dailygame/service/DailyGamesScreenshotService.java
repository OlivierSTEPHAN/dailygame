package com.zytoune.dailygame.service;

import com.zytoune.dailygame.dto.DailyGamesScreenshotsDTO;
import com.zytoune.dailygame.entity.games.DailyGamesScreenshot;
import com.zytoune.dailygame.entity.games.Games;
import com.zytoune.dailygame.repository.games.DailyGamesScreenshotsRepository;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

@Slf4j
@AllArgsConstructor
@Service
public class DailyGamesScreenshotService {

    private DailyGamesScreenshotsRepository dailyGamesScreenshotsRepository;
    private GamesService gamesService;
    private ScreenshotsService screenshotsService;
    private AlternativeNamesService alternativeNamesService;

    public DailyGamesScreenshotsDTO getDailyGames() {
        List<DailyGamesScreenshot> dailyGameScreenshots = dailyGamesScreenshotsRepository.findAll();
        if(dailyGameScreenshots.isEmpty()){
            throw new RuntimeException("No daily games found");
        }

        return DailyGamesScreenshotsDTO.builder()
                .screenshots(dailyGameScreenshots.stream().map(DailyGamesScreenshot::getScreenshot).toList())
                .build();
    }

    public Boolean checkDailyGame(int index, String answer) {

        List<DailyGamesScreenshot> dailyGameScreenshots = dailyGamesScreenshotsRepository.findAll();

        if(dailyGameScreenshots.isEmpty()){
            throw new RuntimeException("No daily games found");
        }

        DailyGamesScreenshot dailyGame = dailyGameScreenshots.get(index);

        if(dailyGame.getName().equalsIgnoreCase(answer)){
            return true;
        }

        for(String alternativeName : dailyGame.getAlternativeNames()){
            if(alternativeName.equalsIgnoreCase(answer)){
                return true;
            }
        }
        return false;
    }

    public void updateDailyGames(){
        log.info("Updating daily games screenshots");

        if(this.gamesService.getNbrGames() == 0 || this.screenshotsService.count() == 0){
            log.info("No games yet");
            return;
        }

        this.dailyGamesScreenshotsRepository.deleteAll();
        for (int i = 0; i < 10; i++) {
            Games game = this.gamesService.findNRandomGames(1).get(0);
            log.info("Game {} : {}", i, game.getName() + " " + game.getScreenshots());
            Random rand = new Random();
            while(StringUtils.isBlank(game.getName()) || CollectionUtils.isEmpty(game.getScreenshots()) || screenshotsService.getScreenshotsById(game.getScreenshots().get(rand.nextInt(game.getScreenshots().size()))) == null){
                log.info("Game {} is not valid", game.getName() + " " + game.getScreenshots());
                game = this.gamesService.findNRandomGames(1).get(0);
            }
            DailyGamesScreenshot dailyGamesScreenshot = DailyGamesScreenshot.builder()
                    .name(game.getName())
                    .alternativeNames(this.alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(game.getAlternativeNames()))
                    .screenshot(this.screenshotsService.generateUrl(this.screenshotsService.getScreenshotsById(game.getScreenshots().get(rand.nextInt(game.getScreenshots().size()))).getImageId())).
                    build();

            this.dailyGamesScreenshotsRepository.save(dailyGamesScreenshot);
            log.info("Daily game {} : {} added", i, dailyGamesScreenshot.getName() + " " + dailyGamesScreenshot.getScreenshot());
        }
    }
}
