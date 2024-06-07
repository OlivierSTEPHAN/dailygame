package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.GameModes;
import com.zytoune.dailygame.repository.games.GameModesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class GameModesService {

    private final GameModesRepository gameModesRepository;

    public List<String> getGameModesByIds(List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return List.of();
        }
        return gameModesRepository.findAllById(ids).stream().map(GameModes::getName).toList();
    }
}
