package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Screenshots;
import com.zytoune.dailygame.model.ImageType;
import com.zytoune.dailygame.repository.games.ScreenshotsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class ScreenshotsService {
    private final ScreenshotsRepository screenshotsRepository;

    public Screenshots getScreenshotsById(int id){
        return this.screenshotsRepository.findById(id).orElse(null);
    }

    public long count() {
        return this.screenshotsRepository.count();
    }

    public String generateUrl(String imageId){
        return "https://images.igdb.com/igdb/image/upload/t_" + ImageType.P1080.getName() + "/" + imageId + ".jpg";
    }
}
