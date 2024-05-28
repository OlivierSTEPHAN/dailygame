package com.zytoune.geogamr.service;

import com.zytoune.geogamr.dto.ResetPasswordDTO;
import com.zytoune.geogamr.entity.Role;
import com.zytoune.geogamr.entity.RoleEnum;
import com.zytoune.geogamr.entity.User;
import com.zytoune.geogamr.entity.Validation;
import com.zytoune.geogamr.repository.RoleRepository;
import com.zytoune.geogamr.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ValidationService validationService;
    private BCryptPasswordEncoder passwordEncoder;

    public void inscription(User user){
        //Vérification du mail
        if(!user.getEmail().contains("@")){
            throw new RuntimeException("Mail invalide, ne contient pas '@'");
        } else if(!user.getEmail().contains(".")){
            throw new RuntimeException("Mail invalide, ne contient pas '.'");
        }

        //Vérification de l'unicité du mail
        Optional<User> userOptional = this.userRepository.findByEmail(user.getEmail());
        if(userOptional.isPresent()){
            throw new RuntimeException("Mail déjà utilisé mon reuf");
        }

        //Vérification du password
        String passwordEncoded = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);

        //Création du role
        Role role = roleRepository.findByLibelle(RoleEnum.USER);
        user.setRole(role);

        user = this.userRepository.save(user);

        this.validationService.register(user);
    }

    public void activation(Map<String, String> activation){
        Validation validation = this.validationService.getValidationByCode(activation.get("code"));
        if(Instant.now().isAfter(validation.getExpiration())){
            throw new RuntimeException("Votre code a expiré");
        }
        validation.setActivation(Instant.now());
        this.validationService.saveOrUpdate(validation);

        User userActive = this.userRepository.findById(validation.getUser().getId()).orElseThrow(() -> new RuntimeException("User introuvable pour le code"));
        userActive.setActive(true);
        this.userRepository.save(userActive);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Aucun user ne correspond à ce username :"+ username));
    }


    public void updatePassword(Map<String, String> param) {
        User user = this.userRepository.findByEmail(param.get("email"))
                .orElseThrow(() -> new UsernameNotFoundException("Aucun user ne correspond à cet email : "+ param.get("email")));
        this.validationService.register(user);
    }

    public void newPassword(ResetPasswordDTO resetPasswordDTO) {
        User user = this.userRepository.findByEmail(resetPasswordDTO.email())
                .orElseThrow(() -> new UsernameNotFoundException("Aucun user ne correspond à cet email : "+ resetPasswordDTO.email()));
        Validation validation = this.validationService.getValidationByCode(resetPasswordDTO.code());

        if(validation.getActivation() != null){
            throw new RuntimeException("Votre code a déjà été utilisé");
        }

        if(validation.getUser().getEmail().equals(user.getEmail())){
            String passwordEncoded = this.passwordEncoder.encode(resetPasswordDTO.password());
            user.setPassword(passwordEncoded);
            this.userRepository.save(user);
            validation.setActivation(Instant.now());
            this.validationService.saveOrUpdate(validation);
        }
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }
}
