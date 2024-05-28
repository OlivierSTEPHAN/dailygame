package com.zytoune.geogamr.controller;

import com.zytoune.geogamr.entity.Game;
import com.zytoune.geogamr.model.GameRequest;
import com.zytoune.geogamr.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("game")
public class GameController {

    private final GameService gameService;

    // GET
    @GetMapping("{id}")
    public ResponseEntity<Game> getGameById(@PathVariable long id) {
        Game game = gameService.getGameById(id);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Game> getGameByName(@PathVariable String name) {
        Game game = gameService.getGameByName(name);
        return ResponseEntity.ok(game);
    }


    // POST
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Game> createGame(@RequestBody GameRequest gameRequest) {
        Game savedGame = gameService.createGame(gameRequest);
        return ResponseEntity.ok(savedGame);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping(consumes = "application/json", produces = "application/json", value = "/{name}")
    public ResponseEntity<Game> updateGame(@PathVariable String name, @RequestBody GameRequest gameRequest) {
        Game savedGame = gameService.updateGame(gameRequest);
        return ResponseEntity.ok(savedGame);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/importGames")
    public ResponseEntity<String> importGames(@RequestBody List<GameRequest> gameRequests) {
        for (GameRequest game : gameRequests) {
            gameService.createGame(game);
        }
        return new ResponseEntity<>("Jeux importés avec succès", HttpStatus.OK);
    }


    // DELETE
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(consumes = "application/json", produces = "application/json", value = "/{name}")
    public void deleteGame(@RequestBody String name) {
        gameService.deleteGame(name);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/nuclear")
    public void deleteGame() {
        gameService.deleteAll();
    }
}
