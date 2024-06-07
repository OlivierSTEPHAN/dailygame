package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.PlayerPerspectives;
import com.zytoune.dailygame.repository.games.PlayerPerspectivesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class PlayerPerspectivesService {

    private final PlayerPerspectivesRepository playerPerspectivesRepository;

    public List<String> getPlayerPerspectivesByIds(List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return List.of();
        }
        return playerPerspectivesRepository.findAllById(ids).stream().map(PlayerPerspectives::getName).toList();
    }
}
