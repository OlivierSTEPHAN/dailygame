package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Genres;
import com.zytoune.dailygame.repository.games.GenresRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class GenresService {

    private final GenresRepository genresRepository;

    public List<String> getGenresByGenreIds(List<Integer> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return List.of();
        }
        return genresRepository.findAllById(ids).stream().map(Genres::getName).toList();
    }
}
