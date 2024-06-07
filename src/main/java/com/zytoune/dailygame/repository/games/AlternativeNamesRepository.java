package com.zytoune.dailygame.repository.games;

import com.zytoune.dailygame.entity.games.AlternativeNames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlternativeNamesRepository extends JpaRepository<AlternativeNames, Integer> {

    @Query(value = "SELECT alternative_name FROM t_alternative_names WHERE game_id = ?1", nativeQuery = true)
    List<AlternativeNames> findAlternativeNamesByGameId(int gameId);
}
