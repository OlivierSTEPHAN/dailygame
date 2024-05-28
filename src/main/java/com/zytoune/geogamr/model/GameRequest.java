package com.zytoune.geogamr.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameRequest {
    private String name;
    private List<String> images;
}