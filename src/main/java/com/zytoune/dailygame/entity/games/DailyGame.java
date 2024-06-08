package com.zytoune.dailygame.entity.games;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_daily_game")
public class DailyGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private List<String> alternativeNames;
    private List<String> genres;
    private List<String> pov;
    private List<String> franchises;
    private List<String> companiesName;
    private List<String> platforms;
    private int year;
    private List<String> gameModes;
    private List<String> gameEngines;
    private Timestamp creation;
}
