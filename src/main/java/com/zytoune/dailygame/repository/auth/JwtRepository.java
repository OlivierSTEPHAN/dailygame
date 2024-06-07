package com.zytoune.dailygame.repository.auth;

import com.zytoune.dailygame.entity.auth.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;


public interface JwtRepository extends JpaRepository<Jwt, Integer> {

    @Query("FROM Jwt j WHERE j.value = :value AND j.desactivated = :desactived AND j.expired = :expired")
    Optional<Jwt> findValidTokenByValue(String value, boolean desactived, boolean expired);

    @Query("FROM Jwt j WHERE j.user.email = :email AND j.desactivated = :desactived AND j.expired = :expired")
    Optional<Jwt> findValidTokenByEmail(String email, boolean desactived, boolean expired);

    @Query("FROM Jwt j WHERE j.user.email = :email")
    Stream<Jwt> findTokensByEmail(String email);

    @Query("FROM Jwt j WHERE j.refreshToken.value = :value")
    Optional<Jwt> findByRefreshTokenValue(String value);


    void deleteAllByExpiredAndDesactivated(boolean expired, boolean desactivated);
}
