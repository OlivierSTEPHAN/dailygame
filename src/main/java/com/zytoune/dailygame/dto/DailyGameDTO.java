package com.zytoune.dailygame.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
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
