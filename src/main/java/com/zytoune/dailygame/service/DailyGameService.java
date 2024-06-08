package com.zytoune.dailygame.service;

import com.zytoune.dailygame.dto.DailyGameDTO;
import com.zytoune.dailygame.entity.games.DailyGame;
import com.zytoune.dailygame.entity.games.Games;
import com.zytoune.dailygame.exception.NoGameFoundException;
import com.zytoune.dailygame.repository.games.DailyGameRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class DailyGameService {

    private DailyGameRepository dailyGameRepository;

    private GamesService gamesService;
    private AlternativeNamesService alternativeNamesService;
    private GenresService genresService;
    private PlayerPerspectivesService playerPerspectivesService;
    private FranchisesService franchisesService;
    private CompaniesService companiesService;
    private PlatformsService platformsService;
    private GameModesService gameModesService;
    private GameEnginesService gameEnginesService;


    private DailyGame returnCompatibleGameOrElseNull(Games game){
        log.info("Daily game update : Checking game {}", game.getName());
        String name = game.getName();
        List<String> alternativeNames = this.alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(game.getAlternativeNames());
        List<String> genres = this.genresService.getGenresByGenreIds(game.getGenres());
        List<String> pov = this.playerPerspectivesService.getPlayerPerspectivesByIds(game.getPlayerPerspectives());
        List<String> franchises = this.franchisesService.getFranchisesByIds(game.getFranchises());
        List<String> companies = this.companiesService.getCompaniesByIds(game.getInvolvedCompanies());
        Integer year = this.getYearFromTimeStamp(game.getFirstReleaseDate());
        List<String> platforms = this.platformsService.getPlatformsByIds(game.getPlatforms());
        List<String> gameModes = this.gameModesService.getGameModesByIds(game.getGameModes());
        List<String> gameEngines = this.gameEnginesService.getGameEngineById(game.getGameEngines());
        if(StringUtils.isBlank(name) || CollectionUtils.isEmpty(alternativeNames) || CollectionUtils.isEmpty(genres) || CollectionUtils.isEmpty(pov) || CollectionUtils.isEmpty(companies) || year == null || CollectionUtils.isEmpty(platforms) || CollectionUtils.isEmpty(gameModes) || CollectionUtils.isEmpty(gameEngines)){
            return null;
        }
        log.info("Daily game update : Game {} is valid", game.getName());
        return DailyGame.builder()
                .name(name)
                .alternativeNames(alternativeNames)
                .genres(genres)
                .pov(pov)
                .franchises(CollectionUtils.isEmpty(franchises) ? List.of(game.getName()) : franchises)
                .companiesName(companies)
                .year(year)
                .platforms(platforms)
                .gameModes(gameModes)
                .gameEngines(gameEngines)
                .build();
    }

    @ReadOnlyProperty
    @Transactional
    public DailyGameDTO getDailyGame() {
        List<DailyGame> dailyGames = this.dailyGameRepository.findAll();
        if(CollectionUtils.isEmpty(dailyGames)){
            throw new RuntimeException("No daily games found");
        }
        DailyGame dailyGame = dailyGames.get(0);
        return DailyGameDTO.builder()
                .name(dailyGame.getName())
                .genres(dailyGame.getGenres())
                .pov(dailyGame.getPov())
                .franchises(dailyGame.getFranchises())
                .companiesName(dailyGame.getCompaniesName())
                .year(dailyGame.getYear())
                .platforms(dailyGame.getPlatforms())
                .gameModes(dailyGame.getGameModes())
                .gameEngines(dailyGame.getGameEngines())
                .build();
    }

    public DailyGameDTO checkAnswer(String gameName) {
        Games gameToCheck = gamesService.findGameByGameName(gameName);
        if (gameToCheck == null) {
            throw new NoGameFoundException("Game not found");
        }

        List<DailyGame> dailyGames = this.dailyGameRepository.findAll();
        if (CollectionUtils.isEmpty(dailyGames)) {
            throw new NoGameFoundException("No daily games found");
        }
        DailyGameDTO dailyGame = this.getDailyGame();

        if (dailyGame.getName().equalsIgnoreCase(gameName)) {
            return dailyGame;
        }

        for (String alternativeName : alternativeNamesService.getAlternativeNamesFromAlternativeNamesId(gameToCheck.getAlternativeNames())) {
            if (alternativeName.equalsIgnoreCase(dailyGame.getName())) {
                return dailyGame;
            }
        }

        return this.mappingGameToDTO(gameToCheck);
    }
    private DailyGameDTO mappingGameToDTO(Games game){
        String name = game.getName();
        List<String> genres = this.genresService.getGenresByGenreIds(game.getGenres());
        List<String> pov = this.playerPerspectivesService.getPlayerPerspectivesByIds(game.getPlayerPerspectives());
        List<String> franchises = this.franchisesService.getFranchisesByIds(game.getFranchises());
        List<String> companies = this.companiesService.getCompaniesByIds(game.getInvolvedCompanies());
        Integer year = this.getYearFromTimeStamp(game.getFirstReleaseDate());
        List<String> platforms = this.platformsService.getPlatformsByIds(game.getPlatforms());
        List<String> gameModes = this.gameModesService.getGameModesByIds(game.getGameModes());
        List<String> gameEngines = this.gameEnginesService.getGameEngineById(game.getGameEngines());

        return DailyGameDTO.builder()
                .name(name)
                .genres(genres)
                .pov(pov)
                .franchises(CollectionUtils.isEmpty(franchises) ? List.of(game.getName()) : franchises)
                .companiesName(companies)
                .year(year)
                .platforms(platforms)
                .gameModes(gameModes)
                .gameEngines(gameEngines)
                .build();
    }


    @Scheduled(cron = "0 */3 * * * *")
    @Profile("dev")
    public void updateDailyGamesDev() {
        log.info("Updating daily game in dev mode");
        updateDailyGames();
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Profile("prod")
    public void updateDailyGamesProd() {
        updateDailyGames();
    }

    private void updateDailyGames(){
        log.info("-------------------");
        log.info("Updating daily game");

        if(this.gamesService.getNbrGames() == 0){
            log.info("No games yet");
            return;
        }

        if(this.dailyGameRepository.count() > 0){
            this.dailyGameRepository.deleteAll();
        }

        Games game = this.gamesService.findNRandomGames(1).get(0);
        DailyGame dailyGame = returnCompatibleGameOrElseNull(game);
        while(dailyGame == null){
            game = this.gamesService.findNRandomGames(1).get(0);
            dailyGame = returnCompatibleGameOrElseNull(game);
        }

        this.dailyGameRepository.save(dailyGame);
        log.info("-------------------");
    }

    private Integer getYearFromTimeStamp(Integer timestamp){
        if(timestamp == null){
            return null;
        }
        LocalDateTime date = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
        return date.getYear();
    }
}
