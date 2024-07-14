package com.zytoune.dailygame.repository.games;

import com.zytoune.dailygame.entity.games.Games;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GamesRepository extends JpaRepository<Games, Integer> {

    @Query(value = "SELECT DISTINCT * FROM t_games WHERE t_games.total_rating_count > :ratingCount AND LOWER(name) NOT LIKE '%collector%' AND category NOT IN (1, 2, 3, 5, 6, 7, 12, 13, 14) ORDER BY RANDOM() LIMIT :n", nativeQuery = true)
    List<Games> findNRandomGames(int n, int ratingCount);

    @Query(value = "SELECT DISTINCT name FROM t_games WHERE LOWER(name) LIKE LOWER(CONCAT('%', :query, '%')) AND LOWER(name) NOT LIKE '%collector%' AND category NOT IN (1, 2, 3, 5, 6, 7, 12, 13, 14)  AND  t_games.total_rating_count > 75 ORDER BY total_rating_count DESC LIMIT 50", nativeQuery = true)
    List<String> findByNameContainingIgnoreCase(String query);

    @Query(value = "SELECT DISTINCT * FROM t_games WHERE name = :gameName LIMIT 1", nativeQuery = true)
    Optional<Games> findByName(String gameName);
}
