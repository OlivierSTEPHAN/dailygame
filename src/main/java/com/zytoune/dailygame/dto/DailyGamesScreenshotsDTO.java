package com.zytoune.dailygame.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyGamesScreenshotsDTO {
    private List<String> screenshots;
}
