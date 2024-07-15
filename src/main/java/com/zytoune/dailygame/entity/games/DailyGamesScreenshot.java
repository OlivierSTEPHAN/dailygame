package com.zytoune.dailygame.entity.games;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_daily_games_screenshot")
public class DailyGamesScreenshot {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private List<String> alternativeNames;
    private String screenshot;
    private List<String> franchises;

    @Builder.Default
    private List<Integer> scores = new ArrayList<>();

}
