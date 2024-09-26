package com.ex.sothat.global.config.jwt;

import com.ex.sothat.domain.dao.Account;
import com.ex.sothat.global.exception.AuthenticationException;
import jakarta.persistence.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }
    public String getEmail(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        }
    }

    public String getNickname(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("nickname", String.class);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        }
    }

    public String getUserRole(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("user_role", String.class);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        }
    }

    public String getUserOauthType(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("oauthType", String.class);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        }
    }

    public Boolean isExpired(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new AuthenticationException("JWT expired");
        }
    }

    public String generateToken(Account account, Long expiredMs) {
        return Jwts.builder()
                .issuer("soThat")
                .subject("JWT Token")
                .claim("email", account.getEmail())
                .claim("nickname", account.getNickname())
                .claim("user_role", account.getUserRole())
                .claim("oauthType", account.getOauthType())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

}

