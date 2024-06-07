package com.zytoune.dailygame.entity.games;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_release_dates")
public class ReleaseDates {
   
    @Id
    private int id;

    private Integer category;
    private Timestamp date;
    private Integer game;
    private String human;
    private Integer m;
    private Integer platform;
    private Integer y;
}
