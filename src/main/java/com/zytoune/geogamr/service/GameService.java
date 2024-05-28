package com.zytoune.geogamr.service;

import com.zytoune.geogamr.entity.Game;
import com.zytoune.geogamr.entity.Image;
import com.zytoune.geogamr.model.GameRequest;
import com.zytoune.geogamr.repository.GameRepository;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;

    @ReadOnlyProperty
    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @ReadOnlyProperty
    public Game getGameByName(String name) {
        return gameRepository.findByName(name).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Game createGame(GameRequest gameRequest) {
        Game game = new Game();
        game.setName(gameRequest.getName());
        List<Image> images = new ArrayList<>();
        gameRequest.getImages().forEach(imageUrl -> {
            Image image = new Image();
            image.setUrl(imageUrl);
            image.setGame(game);
            images.add(image);
        });
        game.setImages(images);
        return gameRepository.save(game);
    }

    @Transactional
    public Game updateGame(GameRequest gameRequest) {
        Game game = gameRepository.findByName(gameRequest.getName()).orElseThrow(EntityNotFoundException::new);
        List<Image> images = new ArrayList<>();
        gameRequest.getImages().forEach(imageUrl -> {
            Image image = new Image();
            image.setUrl(imageUrl);
            image.setGame(game);
            images.add(image);
        });
        game.getImages().addAll(images);
        return gameRepository.save(game);
    }

    public void deleteGame(String name) {
        gameRepository.deleteByName(name);
    }

    public void deleteAll() {
        gameRepository.deleteAll();
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> getRandomGames(int count) {
        return gameRepository.findRandomGames(count);
    }
}
