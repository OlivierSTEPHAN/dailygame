package com.zytoune.geogamr.repository;

import com.zytoune.geogamr.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByName(String name);

    void deleteByName(String name);

    @Query(value = "SELECT * FROM t_games ORDER BY RANDOM() LIMIT ?1", nativeQuery = true)
    List<Game> findRandomGames(int count);
}