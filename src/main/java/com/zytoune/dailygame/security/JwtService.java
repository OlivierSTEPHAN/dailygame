package com.zytoune.dailygame.security;

import com.zytoune.dailygame.entity.auth.Jwt;
import com.zytoune.dailygame.entity.auth.RefreshToken;
import com.zytoune.dailygame.entity.User;
import com.zytoune.dailygame.repository.auth.JwtRepository;
import com.zytoune.dailygame.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.builder;

@Transactional
@AllArgsConstructor
@Slf4j
@Service
public class JwtService {

    public static final String BEARER = "bearer";
    private final String ENCRYPTION_KEY = "29ee619ccc99718daaeb4341a749435a1cf3394a1002014a201e2047fd3637b0";
    private UserService userService;
    private JwtRepository jwtRepository;

    public Map<String, String> generate(String username){
        User user = userService.loadUserByUsername(username);
        this.disableTokens(user);
        Map<String, String> jwtMap = new java.util.HashMap<>(this.generateJwt(user));
        RefreshToken refreshToken = RefreshToken.builder().expired(false).value(UUID.randomUUID().toString()).creation(Instant.now()).expiration(Instant.now().plusSeconds(30*60)).build();
        Jwt jwt = Jwt.builder().value(jwtMap.get(BEARER)).desactivated(false).expired(false).user(user).refreshToken(refreshToken).build();
        this.jwtRepository.save(jwt);
        jwtMap.put("refreshToken", refreshToken.getValue());
        return jwtMap;
    }

    public String extractUsername(String token){
        return this.getClaim(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = this.getClaim(token, Claims::getExpiration);
        return  expiration.before(new Date());
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder().setSigningKey(this.getKey()).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    private Map<String, String> generateJwt(UserDetails user) {

        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + 30 * 60 * 1000;

        Map<String, Object> claims = Map.of(
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, user.getUsername()
        );

        final String bearer = builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expirationTime))
                .setSubject(user.getUsername())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        return Map.of(BEARER, bearer);
    }

    private Key getKey(){
        final byte[] decoder = Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }


    public Jwt tokenByValue(String value) {
        return this.jwtRepository.findValidTokenByValue(value, false, false).orElseThrow(() -> new JwtException("Token not found, or expired"));
    }

    public void logout() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        disableTokens(currentUser);
    }

    private void disableTokens(User user){
        final List<Jwt> jwtList = this.jwtRepository.findTokensByEmail(user.getEmail()).peek(jwt -> {
            jwt.setDesactivated(true);
            jwt.setExpired(true);
        }).toList();
        this.jwtRepository.saveAll(jwtList);
    }

    public Map<String, String> refreshToken(Map<String, String> refreshTokenRequest) {
        final Jwt jwt = this.jwtRepository.findByRefreshTokenValue(refreshTokenRequest.get("refreshToken")).orElseThrow(() -> new JwtException("Token not found for refresh"));
        if(jwt.isExpired() || jwt.getRefreshToken().getExpiration().isBefore(Instant.now())){
            throw new ExpiredJwtException(null, null, "Token expired");
        }
        this.disableTokens(jwt.getUser());
        return this.generate(jwt.getUser().getUsername());
    }

    /**
     * Remove all useless jwt every hour
     */
    @Scheduled(cron = "0 0 */1 * * *")
    public void removeUselessJwt(){
        log.info("Removing useless jwt");
        this.jwtRepository.deleteAllByExpiredAndDesactivated(true, true);
    }
}
