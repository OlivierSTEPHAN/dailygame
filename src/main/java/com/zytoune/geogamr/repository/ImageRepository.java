package com.zytoune.geogamr.repository;


import com.zytoune.geogamr.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByGameId(long gameId);
}
