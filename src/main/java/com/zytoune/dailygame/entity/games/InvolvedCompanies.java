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
@Table(name = "t_involved_companies")
public class InvolvedCompanies {
   
    @Id
    private int id;

    private Integer company;
    private Boolean developer;
    private Integer game;
    private Boolean porting;
    private Boolean publisher;
    private Boolean supporting;
}
