package com.zytoune.dailygame.configuration;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zytoune.dailygame.entity.auth.Role;
import com.zytoune.dailygame.entity.auth.RoleEnum;
import com.zytoune.dailygame.entity.User;
import com.zytoune.dailygame.entity.games.*;
import com.zytoune.dailygame.repository.auth.RoleRepository;
import com.zytoune.dailygame.repository.auth.UserRepository;
import com.zytoune.dailygame.repository.games.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private final AlternativeNamesRepository alternativeNameRepository;
    private final CollectionsRepository collectionsRepository;
    private final CompaniesRepository companyRepository;
    private final EnumGameCategoriesRepository enumGameCategoriesRepository;
    private  final EnumPlatformCategoriesRepository enumPlatformCategoriesRepository;
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


    private ObjectMapper objectMapper;

    /**
     * Initialisation des roles et des utilisateurs ADMIN et MANAGER
     * @return void
     */
    @Bean
    CommandLineRunner init() {
        return args -> {
            if (roleRepository.findByLibelle(RoleEnum.USER) == null) {
                Role userRole = new Role();
                userRole.setLibelle(RoleEnum.USER);
                roleRepository.save(userRole);
            }

            if (roleRepository.findByLibelle(RoleEnum.ADMIN) == null) {
                Role adminRole = new Role();
                adminRole.setLibelle(RoleEnum.ADMIN);
                roleRepository.save(adminRole);
            }

            if (roleRepository.findByLibelle(RoleEnum.MANAGER) == null) {
                Role managerRole = new Role();
                managerRole.setLibelle(RoleEnum.MANAGER);
                roleRepository.save(managerRole);
            }

            if(userRepository.findByUsername("admin").isEmpty() || userRepository.findByUsername("manager").isEmpty()){
                User admin = User.builder().active(true).username("admin").password(passwordEncoder.encode("admin")).email("admin@mail.fr").role(roleRepository.findByLibelle(RoleEnum.ADMIN)).build();
                this.userRepository.save(admin);

                User manager = User.builder().active(true).username("manager").password(passwordEncoder.encode("manager")).email("manager@mail.fr").role(roleRepository.findByLibelle(RoleEnum.MANAGER)).build();
                this.userRepository.save(manager);
            }

            try {
                log.info("Starting data import...");

                log.info("Importing alternative names");
                importDataIfEmpty(alternativeNameRepository, "alternative_names.json", new TypeReference<List<AlternativeNames>>() {});
                log.info("Importing collections");
                importDataIfEmpty(collectionsRepository, "collections.json", new TypeReference<List<Collections>>() {});
                log.info("Importing companies");
                importDataIfEmpty(companyRepository, "companies.json", new TypeReference<List<Companies>>() {});
                log.info("Importing enum game categories");
                importDataIfEmpty(enumGameCategoriesRepository, "enum_game_categories.json", new TypeReference<List<EnumGameCategories>>() {});
                log.info("Importing enum platform categories");
                importDataIfEmpty(enumPlatformCategoriesRepository, "enum_platform_categories.json", new TypeReference<List<EnumPlatformCategories>>() {});
                log.info("Importing enum tags");
                importDataIfEmpty(enumTagRepository, "enum_tags.json", new TypeReference<List<EnumTags>>() {});
                log.info("Importing franchises");
                importDataIfEmpty(franchiseRepository, "franchises.json", new TypeReference<List<Franchises>>() {});
                log.info("Importing game engines");
                importDataIfEmpty(gameEngineRepository, "game_engines.json", new TypeReference<List<GameEngines>>() {});
                log.info("Importing game modes");
                importDataIfEmpty(gameModeRepository, "game_modes.json", new TypeReference<List<GameModes>>() {});
                log.info("Importing games");
                importDataIfEmpty(gameRepository, "games.json", new TypeReference<List<Games>>() {});
                log.info("Importing genres");
                importDataIfEmpty(genreRepository, "genres.json", new TypeReference<List<Genres>>() {});
                log.info("Importing involved companies");
                importDataIfEmpty(involvedCompanyRepository, "involved_companies.json", new TypeReference<List<InvolvedCompanies>>() {});
                log.info("Importing keywords");
                importDataIfEmpty(keywordRepository, "keywords.json", new TypeReference<List<Keywords>>() {});
                log.info("Importing platform families");
                importDataIfEmpty(platformFamilyRepository, "platform_families.json", new TypeReference<List<PlatformFamilies>>() {});
                log.info("Importing platforms");
                importDataIfEmpty(platformRepository, "platforms.json", new TypeReference<List<Platforms>>() {});
                log.info("Importing player perspectives");
                importDataIfEmpty(playerPerspectiveRepository, "player_perspectives.json", new TypeReference<List<PlayerPerspectives>>() {});
                log.info("Importing release dates");
                importDataIfEmpty(releaseDateRepository, "release_dates.json", new TypeReference<List<ReleaseDates>>() {});
                log.info("Importing screenshots");
                importDataIfEmpty(screenshotRepository, "screenshots.json", new TypeReference<List<Screenshots>>() {});
                log.info("Importing themes");
                importDataIfEmpty(themeRepository, "themes.json", new TypeReference<List<Themes>>() {});
                log.info("Data import completed.");

            } catch (Exception e) {
                log.error("Error while reading json files: {}", e.getMessage());
            }
        };
    }

    @Transactional
    private <T> void importDataIfEmpty(JpaRepository<T, ?> repository, String fileName, TypeReference<List<T>> typeReference) {
        if (repository.count() == 0) {
            try (InputStream inputStream = new ClassPathResource("database/data/" + fileName).getInputStream()) {
                List<T> entities = objectMapper.readValue(inputStream, typeReference);
                repository.saveAll(entities);
                log.info("Imported {} records from {}", entities.size(), fileName);
            } catch (IOException e) {
                log.error("Error importing data from {}: ", fileName, e);
            }
        } else {
            log.info("Table for {} is not empty, skipping import.", fileName);
        }
    }

}

