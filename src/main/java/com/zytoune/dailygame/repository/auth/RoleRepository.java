package com.zytoune.dailygame.repository.auth;

import com.zytoune.dailygame.entity.auth.Role;
import com.zytoune.dailygame.entity.auth.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByLibelle(RoleEnum roleEnum);
}
