package com.employee.util;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expirationMs}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String username, Set<String> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    public Set<String> getRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof Collection<?>) {
            return ((Collection<?>) rolesObj)
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
