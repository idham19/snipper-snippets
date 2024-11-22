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
    private String secretKey;
    private SecretKey key;

    private long expirationDate = 24 * 60 * 60 * 1000; // will expire in 24h ;

    private SecretKey getKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }

    public JwtUtil(String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .claim("sub", username).claim("iat", new Date())
                .claim("exp", new Date((System.currentTimeMillis() + expirationDate)))
                .signWith(getKey())
                .compact();
    }

    public String extractUsername(String token) {
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        Claims claims = parser.parseClaimsJws(token).getBody();

        return claims.get("sub", String.class); // Extract the 'sub' claim
    }

}
