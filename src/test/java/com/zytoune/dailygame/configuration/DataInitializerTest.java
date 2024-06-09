package com.zytoune.dailygame.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zytoune.dailygame.repository.games.*;
import com.zytoune.dailygame.service.DailyGameService;
import com.zytoune.dailygame.service.DailyGamesScreenshotService;
import org.junit.jupiter.api.BeforeEach;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DataInitializerTest {

    @Mock
    AlternativeNamesRepository alternativeNameRepository;
    @Mock
    CollectionsRepository collectionsRepository;
    @Mock
    CompaniesRepository companyRepository;
    @Mock
    EnumGameCategoriesRepository enumGameCategoriesRepository;
    @Mock
    EnumPlatformCategoriesRepository enumPlatformCategoriesRepository;
    @Mock
    EnumTagRepository enumTagRepository;
    @Mock
    FranchisesRepository franchiseRepository;
    @Mock
    GameEnginesRepository gameEngineRepository;
    @Mock
    GameModesRepository gameModeRepository;
    @Mock
    GamesRepository gameRepository;
    @Mock
    GenresRepository genreRepository;
    @Mock
    InvolvedCompaniesRepository involvedCompanyRepository;
    @Mock
    KeywordsRepository keywordRepository;
    @Mock
    PlatformFamiliesRepository platformFamilyRepository;
    @Mock
    PlatformsRepository platformRepository;
    @Mock
    PlayerPerspectivesRepository playerPerspectiveRepository;
    @Mock
    ReleaseDatesRepository releaseDateRepository;
    @Mock
    ScreenshotsRepository screenshotRepository;
    @Mock
    ThemesRepository themeRepository;
    @Mock
    DailyGameService dailyGameService;
    @Mock
    DailyGamesScreenshotService dailyGamesScreenshotService;
    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    DataInitializer dataInitializer;

    // Initialize the test logger
    private final TestLogger logger = TestLoggerFactory.getTestLogger(DataInitializer.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldImportDataIfRepositoryIsEmpty() throws Exception {
        when(alternativeNameRepository.count()).thenReturn(0L);
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class))).thenReturn(new ArrayList<>());

        CommandLineRunner runner = dataInitializer.init();
        runner.run();

        verify(alternativeNameRepository).saveAll(any());
    }

    @Test
    void shouldNotImportDataIfRepositoryIsNotEmpty() throws Exception {
        when(alternativeNameRepository.count()).thenReturn(1L);

        CommandLineRunner runner = dataInitializer.init();
        runner.run();

        verify(alternativeNameRepository, never()).saveAll(any());
    }

    @Test
    void shouldThrowIOException() throws Exception {
        when(alternativeNameRepository.count()).thenReturn(1L);
        doThrow(IOException.class).when(objectMapper).readValue(any(InputStream.class), any(TypeReference.class));

        CommandLineRunner runner = dataInitializer.init();
        verify(alternativeNameRepository, never()).saveAll(any());
    }
}
