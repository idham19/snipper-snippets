package com.snipper.snippets.jwt_util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private String secretKey = "ImTheBest";
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

    private long expirationDate = 24 * 60 * 60 * 1000; // will expire in 24h ;

    public String generateToken(String username) {
        return Jwts.builder()
                .claim("sub", username).claim("iat", new Date())
                .claim("exp", new Date((System.currentTimeMillis() + expirationDate)))
                .signWith(key)
                .compact()
                ;
    }
}
