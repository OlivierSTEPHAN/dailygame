package com.zytoune.dailygame.service;

import com.zytoune.dailygame.dto.DailyGamesScreenshotsUrlDTO;
import com.zytoune.dailygame.dto.DailyGamesScreenshotsDTO;
import com.zytoune.dailygame.entity.games.DailyGameScreenshotArchive;
import com.zytoune.dailygame.entity.games.DailyGamesScreenshot;
import com.zytoune.dailygame.entity.games.Games;
import com.zytoune.dailygame.model.ImageType;
import com.zytoune.dailygame.repository.games.DailyGameScreenshotArchiveRepository;
import com.zytoune.dailygame.repository.games.DailyGamesScreenshotsRepository;
import com.zytoune.dailygame.util.GameComparaturUtil;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Slf4j
@AllArgsConstructor
@Service
public class DailyGamesScreenshotService {

    private DailyGamesScreenshotsRepository dailyGamesScreenshotsRepository;
    private DailyGameScreenshotArchiveRepository dailyGameScreenshotArchiveRepository;
    private GamesService gamesService;
    private ScreenshotsService screenshotsService;
    private AlternativeNamesService alternativeNamesService;
    private FranchisesService franchisesService;

    public DailyGamesScreenshotsUrlDTO getDailyGamesUrl() {
        List<DailyGamesScreenshot> dailyGameScreenshots = dailyGamesScreenshotsRepository.findAll();
        if(dailyGameScreenshots.isEmpty()){
            throw new RuntimeException("No daily games found");
        }

        return DailyGamesScreenshotsUrlDTO.builder()
                .screenshots(dailyGameScreenshots.stream().map(DailyGamesScreenshot::getScreenshot).toList())
                .build();
    }


    public DailyGamesScreenshotsDTO getDailyGames() {
        List<DailyGamesScreenshot> dailyGameScreenshots = new ArrayList<>(dailyGamesScreenshotsRepository.findAll());
        if (dailyGameScreenshots.isEmpty()) {
            throw new RuntimeException("No daily games found");
        }

        // Sort by id
        dailyGameScreenshots.sort(Comparator.comparingInt(DailyGamesScreenshot::getId));

        return DailyGamesScreenshotsDTO.builder()
                .name(dailyGameScreenshots.stream().map(DailyGamesScreenshot::getName).toList())
                .url(dailyGameScreenshots.stream().map(DailyGamesScreenshot::getScreenshot).toList())
                .build();
    }


    public Boolean checkDailyGame(int index, String answer) {

        List<DailyGamesScreenshot> dailyGameScreenshots = dailyGamesScreenshotsRepository.findAll();

        if(dailyGameScreenshots.isEmpty()){
            throw new RuntimeException("No daily games found");
        }

        DailyGamesScreenshot dailyGame = dailyGameScreenshots.get(index);

        // Vérif par le nom du jeu
        if(GameComparaturUtil.compareGameNames(dailyGame.getName(), answer)){
            return true;
        }

        // Vérif par les noms alternatifs
        for(String alternativeName : dailyGame.getAlternativeNames()){
            if(GameComparaturUtil.compareGameNames(alternativeName, answer)){
                return true;
            }
        }

        // Vérif par les franchises
        for(String franchise : dailyGame.getFranchises()){
            if(GameComparaturUtil.compareGameNames(franchise, answer)){
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
        if(this.dailyGamesScreenshotsRepository.count() > 0){

            List<DailyGamesScreenshot> dailyGamesScreenshotList = this.dailyGamesScreenshotsRepository.findAll();

            int currentDate = Integer.parseInt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")));

            for(DailyGamesScreenshot dailyGamesScreenshot : dailyGamesScreenshotList){
                DailyGameScreenshotArchive dailyGameScreenshotArchive = DailyGameScreenshotArchive.builder()
                        .date(currentDate)
                        .name(dailyGamesScreenshot.getName())
                        .screenshot(dailyGamesScreenshot.getScreenshot())
                        .scores(dailyGamesScreenshot.getScores())
                        .build();
                this.dailyGameScreenshotArchiveRepository.save(dailyGameScreenshotArchive);
            }

            this.dailyGamesScreenshotsRepository.deleteAll();
        }

        for (int i = 0; i < 10; i++) {
            Games game = this.gamesService.findNRandomGames(1, 75).get(0);
            log.info("Game {} : {}", i, game.getName() + " " + game.getScreenshots());
            Random rand = new Random();
            while(StringUtils.isBlank(game.getName()) || CollectionUtils.isEmpty(game.getScreenshots()) || screenshotsService.getScreenshotsById(game.getScreenshots().get(rand.nextInt(game.getScreenshots().size()))) == null){
                log.info("Game {} is not valid", game.getName() + " " + game.getScreenshots());
                game = this.gamesService.findNRandomGames(1, 75).get(0);
            }
            DailyGamesScreenshot dailyGamesScreenshot = DailyGamesScreenshot.builder()
                    .name(game.getName())
                    .alternativeNames(this.alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(game.getAlternativeNames()))
                    .screenshot(this.screenshotsService.generateUrl(
                            this.screenshotsService.getScreenshotsById(game.getScreenshots().get(rand.nextInt(game.getScreenshots().size()))).getImageId()
                            , ImageType.P1080)
                    ).franchises(franchisesService.getFranchisesByIds(game.getFranchises()))
                    .build();

            this.dailyGamesScreenshotsRepository.save(dailyGamesScreenshot);
            log.info("Daily game {} : {} added", i, dailyGamesScreenshot.getName() + " " + dailyGamesScreenshot.getScreenshot());
        }
    }


    public void addScore(List<Integer> dailyGamesScore) {
        List<DailyGamesScreenshot> dailyGameScreenshots = dailyGamesScreenshotsRepository.findAll();
        if(dailyGameScreenshots.isEmpty()){
            throw new RuntimeException("No daily games found");
        }

        if(dailyGameScreenshots.size() != dailyGamesScore.size()){
            throw new RuntimeException("Daily games and scores length don't match, no score will be added");
        }

        for(int i = 0; i < dailyGamesScore.size(); i++){
            DailyGamesScreenshot dailyGame = dailyGameScreenshots.get(i);
            dailyGame.getScores().add(dailyGamesScore.get(i));
            dailyGamesScreenshotsRepository.save(dailyGame);
        }
    }

    public List<Integer> getScore() {
        List<DailyGamesScreenshot> dailyGameScreenshots = dailyGamesScreenshotsRepository.findAll();
        if(dailyGameScreenshots.isEmpty()){
            throw new RuntimeException("No daily games found");
        }

        // for every daily game, get the average score, and return the list of all average scores, if no score, return 0
        if (dailyGameScreenshots.stream().allMatch(dailyGame -> dailyGame.getScores().isEmpty())) {
            return dailyGameScreenshots.stream().map(dailyGame -> 0).toList();
        }

        return dailyGameScreenshots.stream().map(dailyGame -> dailyGame.getScores().stream().mapToInt(Integer::intValue).sum() / dailyGame.getScores().size()).toList();
    }
}
