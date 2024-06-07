package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.GameEngines;
import com.zytoune.dailygame.repository.games.GameEnginesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class GameEnginesService {

    private GameEnginesRepository gameEnginesRepository;

    public List<String> getGameEngineById(List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return List.of();
        }
        return gameEnginesRepository.findAllById(ids).stream().map(GameEngines::getName).toList();
    }
}
