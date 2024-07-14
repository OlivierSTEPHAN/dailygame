package com.zytoune.dailygame.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class DailyGamesScreenshotScoreDTO {
    List<Integer> dailyGamesScore;
}
