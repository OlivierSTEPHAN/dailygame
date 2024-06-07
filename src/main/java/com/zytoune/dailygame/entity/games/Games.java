package com.zytoune.dailygame.entity.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_games")
public class Games {
   
    @Id
    private int id;

    private String name;
    @Column(name="alternative_names")
    @JsonProperty("alternative_names")
    private List<Integer> alternativeNames;
    private Integer category;
    private Integer collection;
    private List<Integer> collections;
    @Column(name="first_release_date")
    @JsonProperty("first_release_date")
    private Integer firstReleaseDate;
    private List<Integer> franchises;
    @Column(name="game_engines")
    @JsonProperty("game_engines")
    private List<Integer> gameEngines;
    @Column(name="game_modes")
    @JsonProperty("game_modes")
    private List<Integer> gameModes;
    private List<Integer> genres;
    @Column(name="involved_companies")
    @JsonProperty("involved_companies")
    private List<Integer> involvedCompanies;
    private List<Integer> keywords;
    @Column(name="parent_game")
    @JsonProperty("parent_game")
    private Integer parentGame;
    private List<Integer> platforms;
    @Column(name="player_perspectives")
    @JsonProperty("player_perspectives")
    private List<Integer> playerPerspectives;
    private List<Integer> ports;
    private List<Integer> screenshots;
    @Column(name="release_dates")
    @JsonProperty("release_dates")
    private List<Integer> releaseDates;
    private List<Integer> remakes;
    private List<Integer> remasters;
    @Column(name="similar_games")
    @JsonProperty("similar_games")
    private List<Integer> similarGames;
    private String slug;
    @Column(name = "storyline", columnDefinition = "TEXT")
    private String storyline;
    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;
    private List<Integer> tags;
    private List<Integer> themes;
    @Column(name = "total_rating")
    @JsonProperty("total_rating")
    private Integer totalRating;
    @Column(name = "total_rating_count")
    @JsonProperty("total_rating_count")
    private Integer totalRatingCount;
    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private Timestamp updatedAt;
    private String url;
    @Column(name = "version_parent")
    @JsonProperty("version_parent")
    private Integer versionParent;
    @Column(name = "version_title")
    @JsonProperty("version_title")
    private String versionTitle;

}
