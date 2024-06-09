package com.zytoune.dailygame.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zytoune.dailygame.entity.games.*;
import com.zytoune.dailygame.repository.games.*;
import com.zytoune.dailygame.service.DailyGameService;
import com.zytoune.dailygame.service.DailyGamesScreenshotService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Component
public class DataInitializer {

    private final AlternativeNamesRepository alternativeNameRepository;
    private final CollectionsRepository collectionsRepository;
    private final CompaniesRepository companyRepository;
    private final EnumGameCategoriesRepository enumGameCategoriesRepository;
    private final EnumPlatformCategoriesRepository enumPlatformCategoriesRepository;
    private final EnumTagRepository enumTagRepository;
    private final FranchisesRepository franchiseRepository;
    private final GameEnginesRepository gameEngineRepository;
    private final GameModesRepository gameModeRepository;
    private final GamesRepository gameRepository;
    private final GenresRepository genreRepository;
    private final InvolvedCompaniesRepository involvedCompanyRepository;
    private final KeywordsRepository keywordRepository;
    private final PlatformFamiliesRepository platformFamilyRepository;
    private final PlatformsRepository platformRepository;
    private final PlayerPerspectivesRepository playerPerspectiveRepository;
    private final ReleaseDatesRepository releaseDateRepository;
    private final ScreenshotsRepository screenshotRepository;
    private final ThemesRepository themeRepository;

    private DailyGameService dailyGameService;
    private DailyGamesScreenshotService dailyGamesScreenshotService;

    private ObjectMapper objectMapper;

    @Bean
    CommandLineRunner init() {
        return args -> {
            try {
                log.info("Starting data import...");

                log.info("Importing alternative names");
                importDataIfEmpty(alternativeNameRepository, "alternative_names", new TypeReference<List<AlternativeNames>>() {});
                log.info("Importing collections");
                importDataIfEmpty(collectionsRepository, "collections", new TypeReference<List<Collections>>() {});
                log.info("Importing companies");
                importDataIfEmpty(companyRepository, "companies", new TypeReference<List<Companies>>() {});
                log.info("Importing enum game categories");
                importDataIfEmpty(enumGameCategoriesRepository, "enum_game_categories", new TypeReference<List<EnumGameCategories>>() {});
                log.info("Importing enum platform categories");
                importDataIfEmpty(enumPlatformCategoriesRepository, "enum_platform_categories", new TypeReference<List<EnumPlatformCategories>>() {});
                log.info("Importing enum tags");
                importDataIfEmpty(enumTagRepository, "enum_tags", new TypeReference<List<EnumTags>>() {});
                log.info("Importing franchises");
                importDataIfEmpty(franchiseRepository, "franchises", new TypeReference<List<Franchises>>() {});
                log.info("Importing game engines");
                importDataIfEmpty(gameEngineRepository, "game_engines", new TypeReference<List<GameEngines>>() {});
                log.info("Importing game modes");
                importDataIfEmpty(gameModeRepository, "game_modes", new TypeReference<List<GameModes>>() {});
                log.info("Importing games");
                importDataIfEmpty(gameRepository, "games", new TypeReference<List<Games>>() {});
                log.info("Importing genres");
                importDataIfEmpty(genreRepository, "genres", new TypeReference<List<Genres>>() {});
                log.info("Importing involved companies");
                importDataIfEmpty(involvedCompanyRepository, "involved_companies", new TypeReference<List<InvolvedCompanies>>() {});
                log.info("Importing keywords");
                importDataIfEmpty(keywordRepository, "keywords", new TypeReference<List<Keywords>>() {});
                log.info("Importing platform families");
                importDataIfEmpty(platformFamilyRepository, "platform_families", new TypeReference<List<PlatformFamilies>>() {});
                log.info("Importing platforms");
                importDataIfEmpty(platformRepository, "platforms", new TypeReference<List<Platforms>>() {});
                log.info("Importing player perspectives");
                importDataIfEmpty(playerPerspectiveRepository, "player_perspectives", new TypeReference<List<PlayerPerspectives>>() {});
                log.info("Importing release dates");
                importDataIfEmpty(releaseDateRepository, "release_dates", new TypeReference<List<ReleaseDates>>() {});
                log.info("Importing screenshots");
                importDataIfEmpty(screenshotRepository, "screenshots", new TypeReference<List<Screenshots>>() {});
                log.info("Importing themes");
                importDataIfEmpty(themeRepository, "themes", new TypeReference<List<Themes>>() {});
                log.info("Data import completed.");

                // Initialisation des jeux du jour
                dailyGameService.updateDailyGames();
                dailyGamesScreenshotService.updateDailyGames();
            } catch (Exception e) {
                log.error("Error while reading json files: {}", e.getMessage());
            }
        };
    }

    @Transactional
    private <T> void importDataIfEmpty(JpaRepository<T, ?> repository, String directoryName, TypeReference<List<T>> typeReference) {
        if (repository.count() == 0) {
            try {
                Path dirPath = Paths.get(new ClassPathResource("database/data/" + directoryName).getURI());
                List<T> allEntities = new ArrayList<>();

                Files.walk(dirPath)
                        .filter(Files::isRegularFile)
                        .forEach(filePath -> {
                            try (InputStream inputStream = Files.newInputStream(filePath)) {
                                List<T> entities = objectMapper.readValue(inputStream, typeReference);
                                allEntities.addAll(entities);
                            } catch (IOException e) {
                                log.error("Error importing data from {}: ", filePath.getFileName(), e);
                            }
                        });

                repository.saveAll(allEntities);
                log.info("Imported {} records from {}", allEntities.size(), directoryName);
            } catch (IOException e) {
                log.error("Error reading directory {}: ", directoryName, e);
            }
        } else {
            log.info("Table for {} is not empty, skipping import.", directoryName);
        }
    }

}
