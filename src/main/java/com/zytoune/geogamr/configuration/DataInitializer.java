package com.zytoune.geogamr.configuration;


import com.zytoune.geogamr.entity.Role;
import com.zytoune.geogamr.entity.RoleEnum;
import com.zytoune.geogamr.entity.User;
import com.zytoune.geogamr.repository.RoleRepository;
import com.zytoune.geogamr.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * Initialisation des roles et des utilisateurs ADMIN et MANAGER
     * @return void
     */
    @Bean
    CommandLineRunner init() {
        return args -> {
            if (roleRepository.findByLibelle(RoleEnum.USER) == null) {
                Role userRole = new Role();
                userRole.setLibelle(RoleEnum.USER);
                roleRepository.save(userRole);
            }

            if (roleRepository.findByLibelle(RoleEnum.ADMIN) == null) {
                Role adminRole = new Role();
                adminRole.setLibelle(RoleEnum.ADMIN);
                roleRepository.save(adminRole);
            }

            if (roleRepository.findByLibelle(RoleEnum.MANAGER) == null) {
                Role managerRole = new Role();
                managerRole.setLibelle(RoleEnum.MANAGER);
                roleRepository.save(managerRole);
            }

            if(userRepository.findByUsername("admin").isEmpty() || userRepository.findByUsername("manager").isEmpty()){
                User admin = User.builder().active(true).username("admin").password(passwordEncoder.encode("admin")).email("admin@mail.fr").role(roleRepository.findByLibelle(RoleEnum.ADMIN)).build();
                this.userRepository.save(admin);

                User manager = User.builder().active(true).username("manager").password(passwordEncoder.encode("manager")).email("manager@mail.fr").role(roleRepository.findByLibelle(RoleEnum.MANAGER)).build();
                this.userRepository.save(manager);
            }
        };
    }
}

