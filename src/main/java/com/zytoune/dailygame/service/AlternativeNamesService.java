package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.AlternativeNames;
import com.zytoune.dailygame.repository.games.AlternativeNamesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class AlternativeNamesService {
    private AlternativeNamesRepository alternativeNamesRepository;

    public List<String> getAlternativeNamesFromAlternativeNamesId(List<Integer> alternativeNamesId) {
        if(CollectionUtils.isEmpty(alternativeNamesId)){
            return List.of();
        }
        return alternativeNamesRepository.findAllById(alternativeNamesId).stream().map(AlternativeNames::getName).toList();
    }
}
