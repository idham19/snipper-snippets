package com.snipper.snippets.jwt_util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${secret.key}")
    private final String secretKey;
    private SecretKey key;

    private final long expirationDate = 24 * 60 * 60 * 1000; // will expire in 24h ;

    private SecretKey getKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }

    public JwtUtil(String secretKey) {
        this.secretKey = secretKey;
    }

    //method to generate token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationDate))
                .signWith(getKey())
                .compact();
    }

    public String extractUsername(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build();
        Claims claims = parser.parseClaimsJws(token).getBody();

        return claims.get("sub", String.class); // Extract the 'sub' claim
    }

    public Date extractExpirationDate(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getKey()).build();
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getExpiration();
    }
}
