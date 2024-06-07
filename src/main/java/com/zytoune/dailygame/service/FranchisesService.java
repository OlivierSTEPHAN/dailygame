package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.games.Franchises;
import com.zytoune.dailygame.repository.games.FranchisesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@AllArgsConstructor
@Service
public class FranchisesService {

        private final FranchisesRepository franchisesRepository;

        public List<String> getFranchisesByIds(List<Integer> ids) {
            if(CollectionUtils.isEmpty(ids)){
                return List.of();
            }
            return franchisesRepository.findAllById(ids).stream().map(Franchises::getName).toList();
        }
}
