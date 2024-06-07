package com.zytoune.dailygame.entity.games;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "t_platforms")
public class Platforms {
   
    @Id
    private int id;

    private String abbreviation;
    @Column(name = "alternative_name")
    @JsonProperty("alternative_name")
    private String alternativeName;
    private Integer category;
    private String name;
    @Column(name = "platform_family")
    @JsonProperty("platform_family")
    private Integer platformFamily;
    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;
}
