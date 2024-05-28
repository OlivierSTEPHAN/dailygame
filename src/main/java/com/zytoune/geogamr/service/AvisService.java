package com.zytoune.geogamr.service;

import com.zytoune.geogamr.entity.Avis;
import com.zytoune.geogamr.repository.AvisRepository;
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
