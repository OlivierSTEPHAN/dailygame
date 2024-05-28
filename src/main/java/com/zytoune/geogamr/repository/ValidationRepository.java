package com.zytoune.geogamr.repository;

import com.zytoune.geogamr.entity.User;
import com.zytoune.geogamr.entity.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface ValidationRepository extends JpaRepository<Validation, Integer> {
    Optional<Validation> findByCode(String code);

    Optional<Validation> findByUser(User user);

    void deleteAllByExpirationBefore(Instant now);
}
