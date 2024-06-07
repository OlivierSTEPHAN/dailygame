package com.zytoune.dailygame.entity.games;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_alternatives_names")
public class AlternativeNames {
    @Id
    private int id;

    private String comment;
    private Integer game;
    private String name;
}
