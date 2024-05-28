package com.zytoune.geogamr.repository;

import com.zytoune.geogamr.entity.Role;
import com.zytoune.geogamr.entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByLibelle(RoleEnum roleEnum);
}
