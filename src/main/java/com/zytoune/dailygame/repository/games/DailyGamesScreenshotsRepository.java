package com.zytoune.dailygame.repository.games;

import com.zytoune.dailygame.entity.games.DailyGamesScreenshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyGamesScreenshotsRepository extends JpaRepository<DailyGamesScreenshot, Integer> {

    List<DailyGamesScreenshot> findAllByOrderByIdAsc();
}
