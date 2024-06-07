package com.zytoune.dailygame.controller;

import com.zytoune.dailygame.dto.AuthentificationDTO;
import com.zytoune.dailygame.dto.ResetPasswordDTO;
import com.zytoune.dailygame.entity.User;
import com.zytoune.dailygame.security.JwtService;
import com.zytoune.dailygame.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@AllArgsConstructor
@RequestMapping("user")
@RestController
public class UserController {

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    @PostMapping(path = "register")
    public void register(@RequestBody User user){
        log.info("Inscription de l'utilisateur :{}", user.getUsername());
        this.userService.inscription(user);
    }

    @PostMapping(path = "activate")
    public void activation(@RequestBody Map<String, String> activation){
        log.info("Activation de l'utilisateur :{}", activation.get("code"));
        this.userService.activation(activation);
    }

    @PostMapping(path = "login")
    public Map<String, String> login(@RequestBody AuthentificationDTO authentificationDTO){
        log.info("Connexion de l'utilisateur : {}", authentificationDTO.username());
        Authentication authentification = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authentificationDTO.username(), authentificationDTO.password()));

        if(authentification.isAuthenticated()){
            return this.jwtService.generate(authentificationDTO.username());
        }
        return null;
    }

    @PostMapping(path = "refresh-token")
    public @ResponseBody Map<String, String>  refreshToken(@RequestBody Map<String, String> refreshTokenRequest){
        log.info("Refresh du token");
       return this.jwtService.refreshToken(refreshTokenRequest);
    }

    @PostMapping(path = "update-password")
    public void  updatePassword(@RequestBody Map<String, String> param){
        log.info("Update du password");
        this.userService.updatePassword(param);
    }

    @PostMapping(path = "new-password")
    public void  newPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
        log.info("Nouveau password");
        this.userService.newPassword(resetPasswordDTO);
    }

    @PostMapping(path = "logout")
    public void logout(){
        log.info("Déconnexion de l'utilisateur");
        this.jwtService.logout();
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "users")
    public List<User> getUsers(){
        return this.userService.getUsers();
    }
}
