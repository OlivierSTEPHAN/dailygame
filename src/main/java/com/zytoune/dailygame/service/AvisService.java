package com.zytoune.dailygame.service;

import com.zytoune.dailygame.entity.Avis;
import com.zytoune.dailygame.repository.AvisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class AvisService {

    private final AvisRepository avisRepository;

    public void creerAvis(Avis avis){
        this.avisRepository.save(avis);
    }

    public List<Avis> getAvis() {
        return avisRepository.findAll();
    }
}
