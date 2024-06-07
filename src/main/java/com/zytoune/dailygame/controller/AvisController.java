package com.zytoune.dailygame.controller;

import com.zytoune.dailygame.entity.Avis;
import com.zytoune.dailygame.entity.User;
import com.zytoune.dailygame.security.JwtService;
import com.zytoune.dailygame.service.AvisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Slf4j
@RequestMapping("avis")
@RestController
public class AvisController {
    private final AvisService avisService;
    private final JwtService jwtService;
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void creer(@RequestBody Avis avis){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        avis.setUser(user);
        this.avisService.creerAvis(avis);
    }

    @PostMapping(path = "logout")
    public void logout(){
        log.info("Déconnexion de l'utilisateur");
        this.jwtService.logout();
    }
    @GetMapping
    public List<Avis> liste(){
        return this.avisService.getAvis();
    }
}
