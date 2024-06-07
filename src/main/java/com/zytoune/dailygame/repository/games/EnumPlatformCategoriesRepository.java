package com.zytoune.dailygame.repository.games;

import com.zytoune.dailygame.entity.games.EnumPlatformCategories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnumPlatformCategoriesRepository extends JpaRepository<EnumPlatformCategories, Integer> {
}
