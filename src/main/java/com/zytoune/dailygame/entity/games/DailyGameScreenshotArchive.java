package com.zytoune.dailygame.entity.games;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "t_daily_game_screenshot_archive")
public class DailyGameScreenshotArchive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int date;
    private String name;
    private String screenshot;

    @Builder.Default
    private List<Integer> scores = new ArrayList<>();
}
