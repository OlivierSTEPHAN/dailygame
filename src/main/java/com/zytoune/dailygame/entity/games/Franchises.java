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
@Table(name = "t_franchises")
public class Franchises {
   
    @Id
    private int id;

    private String name;
    private String slug;
}
