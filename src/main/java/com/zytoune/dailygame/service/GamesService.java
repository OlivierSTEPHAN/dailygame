package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Games;
import com.zytoune.dailygame.repository.games.GamesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class GamesService {

    private final GamesRepository gamesRepository;

    public List<Games> getNRandomGames(int n){
        return this.gamesRepository.findNRandomGames(n, 50);
    }

    public List<String> findGameNames(String query) {

        return gamesRepository.findByNameContainingIgnoreCase(query);
    }

    public long getNbrGames() {
        return (int) this.gamesRepository.count();
    }

    public List<Games> findNRandomGames(int n, int ratingCount) {
        return this.gamesRepository.findNRandomGames(n, ratingCount);
    }

    public Games findGameByGameName(String gameName) {
        return this.gamesRepository.findByName(gameName).orElse(null);
    }
}
