package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Platforms;
import com.zytoune.dailygame.repository.games.PlatformsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class PlatformsService {

    private PlatformsRepository platformsRepository;

    public List<String> getPlatformsByIds(List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return List.of();
        }
        return platformsRepository.findAllById(ids).stream().map(Platforms::getName).toList();
    }
}
