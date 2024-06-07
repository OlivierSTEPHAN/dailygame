package com.zytoune.dailygame.repository.games;

import com.zytoune.dailygame.entity.games.EnumTags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnumTagRepository extends JpaRepository<EnumTags, Integer> {
}
