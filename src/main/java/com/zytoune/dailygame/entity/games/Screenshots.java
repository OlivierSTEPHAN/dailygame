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
@Table(name = "t_screenshots")
public class Screenshots {
   
    @Id
    private int id;

    @Column(name = "alpha_channel")
    @JsonProperty("alpha_channel")
    private Boolean alphaChannel;
    private Boolean animated;
    private Integer Game;
    private Integer height;
    @Column(name = "image_id")
    @JsonProperty("image_id")
    private String imageId;
    private String url;
    private Integer width;

}
