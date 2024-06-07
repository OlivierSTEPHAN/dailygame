package com.zytoune.dailygame.repository.games;

import com.zytoune.dailygame.entity.games.Genres;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenresRepository extends JpaRepository<Genres, Integer> {
}
