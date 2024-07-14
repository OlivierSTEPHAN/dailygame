package com.zytoune.dailygame.entity.games;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_daily_game_archive")
public class DailyGameArchive {
    @Id
    private int id;

    private String name;
    private List<Integer> scores;
}
