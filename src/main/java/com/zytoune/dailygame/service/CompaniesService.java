package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Companies;
import com.zytoune.dailygame.entity.games.InvolvedCompanies;
import com.zytoune.dailygame.repository.games.CompaniesRepository;
import com.zytoune.dailygame.repository.games.InvolvedCompaniesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class CompaniesService {

    private final CompaniesRepository companiesRepository;
    private final InvolvedCompaniesRepository involvedCompaniesRepository;

    public List<String> getCompaniesByIds(List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return List.of();
        }
        List<Integer> companiesId = involvedCompaniesRepository.findAllById(ids).stream().map(InvolvedCompanies::getCompany).toList();
        if(CollectionUtils.isEmpty(companiesId)){
            return List.of();
        }
        return companiesRepository.findAllById(companiesId).stream().map(Companies::getName).toList();
    }
}
