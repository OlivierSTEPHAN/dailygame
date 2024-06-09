package com.zytoune.dailygame.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyGameDTO {
    private String name;
    private List<String> genres;
    private List<String> pov;
    private List<String> franchises;
    private List<String> companiesName;
    private List<String> platforms;
    private int year;
    private List<String> gameModes;
    private List<String> gameEngines;
}
