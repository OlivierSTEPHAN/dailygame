package com.zytoune.dailygame.controller;

import com.zytoune.dailygame.entity.games.Games;
import com.zytoune.dailygame.service.GamesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RequestMapping("games")
@RestController
public class GamesController {

    private final GamesService gamesService;

    @GetMapping(path = "random/{n}")
    public ResponseEntity<List<Games>> getNRandomGames(@PathVariable int n){
        log.info("Récupération de {} jeux aléatoires", n);
        return ResponseEntity.ok(gamesService.getNRandomGames(n));
    }

    @GetMapping("/autocomplete")
    public List<String> autocomplete(@RequestParam String name) {
        return gamesService.findGameNames(name);
    }

}
