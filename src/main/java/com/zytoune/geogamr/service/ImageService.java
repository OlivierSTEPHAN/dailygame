package com.zytoune.geogamr.service;


import com.zytoune.geogamr.dto.ImagesByGameDTO;
import com.zytoune.geogamr.dto.RandomImageAndGameDTO;
import com.zytoune.geogamr.entity.Game;
import com.zytoune.geogamr.entity.Image;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
@Service
public class ImageService {

    private final GameService gameService;

    public ImagesByGameDTO getImagesByGame(String name) {
        Game game = gameService.getGameByName(name);
        List<String> imageUrls = game.getImages().stream().map(Image::getUrl).toList();
        return new ImagesByGameDTO(imageUrls);
    }

    public List<RandomImageAndGameDTO> getRandomImages(int count) {
        return gameService.getRandomGames(count).stream().map(game -> {
            String imageUrl = game.getImages().get(
                    ThreadLocalRandom.current().nextInt(0, game.getImages().size())
            ).getUrl(); // Random
            return new RandomImageAndGameDTO(game.getName(), imageUrl);
        }).toList();
    }

}
