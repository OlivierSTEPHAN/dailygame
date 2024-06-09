package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Companies;
import com.zytoune.dailygame.entity.games.InvolvedCompanies;
import com.zytoune.dailygame.repository.games.CompaniesRepository;
import com.zytoune.dailygame.repository.games.InvolvedCompaniesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CompaniesServiceTest {
    @Mock
    private CompaniesRepository companiesRepository;

    @Mock
    private InvolvedCompaniesRepository involvedCompaniesRepository;

    @InjectMocks
    private CompaniesService companiesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnCompanyNames() {
        List<Integer> ids = Arrays.asList(1, 2);
        List<InvolvedCompanies> involvedCompanies = Arrays.asList(new InvolvedCompanies(1, 1, true, 1, true, true, true), new InvolvedCompanies(2, 2, true, 2, true, true, true));
        List<Companies> companies = Arrays.asList(new Companies(1, null, 1, "description1", "name1", null, "slug", Timestamp.from(Instant.now()), 1, "url", List.of(1,2)), new Companies(2, null, 2, "description2", "name2", null, "slug", Timestamp.from(Instant.now()), 2, "url", List.of(1,2)));
        when(involvedCompaniesRepository.findAllById(ids)).thenReturn(involvedCompanies);
        when(companiesRepository.findAllById(Arrays.asList(1, 2))).thenReturn(companies);

        List<String> result = companiesService.getCompaniesByIds(ids);

        assertEquals(2, result.size());
        assertEquals("name1", result.get(0));
        assertEquals("name2", result.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenNoIdsProvided() {
        List<String> result = companiesService.getCompaniesByIds(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenNoCompaniesFound() {
        List<Integer> ids = Arrays.asList(1, 2);
        when(involvedCompaniesRepository.findAllById(ids)).thenReturn(List.of());

        List<String> result = companiesService.getCompaniesByIds(ids);

        assertTrue(result.isEmpty());
    }
}