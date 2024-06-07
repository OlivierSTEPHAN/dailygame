package com.zytoune.dailygame.repository.games;

import com.zytoune.dailygame.entity.games.DailyGame;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DailyGameRepository extends JpaRepository<DailyGame, Integer>{

    @Modifying
    @Query(value = "DELETE FROM t_daily_game", nativeQuery = true)
    void deleteAll();

    @Nonnull
    @Query(value = "SELECT * FROM t_daily_game", nativeQuery = true)
    List<DailyGame> findAll();
}
