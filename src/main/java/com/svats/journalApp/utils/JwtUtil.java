package com.svats.journalApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public Claims validateAndGetClaims(String token) {
        // Will verify token signature here
        Claims claims = extractAllClaims(token);

        // Claim validations like expiration, username presence, etc.
        if (validateExpiration(claims) && validateIfUsernameIsPresent(claims)) return claims;
        else throw new JwtException("Invalid token values");
    }

    // Claim validations

    private Boolean validateIfUsernameIsPresent(Claims claims) {
        return claims.getSubject() != null;
    }

    private Boolean validateExpiration(Claims claims) {
        return claims.getExpiration().after(new Date());
    }

    // Other private methods

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 60 minutes expiration time
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Will verify token signature here
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";

}
