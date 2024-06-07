package com.zytoune.dailygame.repository.games;

import com.zytoune.dailygame.entity.games.EnumGameCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnumGameCategoriesRepository extends JpaRepository<EnumGameCategories, Integer> {
}
