package com.zytoune.geogamr.controller;

import com.zytoune.geogamr.dto.ImagesByGameDTO;
import com.zytoune.geogamr.dto.RandomImageAndGameDTO;
import com.zytoune.geogamr.entity.Game;
import com.zytoune.geogamr.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("image")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/random")
    public @ResponseBody ResponseEntity<List<RandomImageAndGameDTO>> getRandomImage(@RequestBody Map<String, Integer> params) {
        List<RandomImageAndGameDTO> imagesByGameDTO = imageService.getRandomImages(params.get("count"));
        return ResponseEntity.ok(imagesByGameDTO);
    }

    @GetMapping("/gameName/{name}")
    public @ResponseBody ResponseEntity<ImagesByGameDTO> getGameByName(@PathVariable String name) {
        ImagesByGameDTO imagesByGameDTO = imageService.getImagesByGame(name);
        return ResponseEntity.ok(imagesByGameDTO);
    }
}
