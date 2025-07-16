package com.bict.auth_api.components;

import java.util.Collections;
import java.util.Date;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
    private final String secretKey = "fjdanjfidsanifndsaifnjdsinfjdsinfjsdnjfidsnjfinsdji";
    private final long expiration = 1000 * 60 * 60 * 24;

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        String username = claims.getSubject();
        String role = claims.get("role", String.class);
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }

    public String generateToken(String username, String role) {
        try {
            System.out.println("▶ generateToken called");
            System.out.println("▶ username = " + username);
            System.out.println("▶ role = " + role);
            System.out.println("▶ expiration = " + expiration);
            System.out.println("▶ secretKey.length = " + secretKey.length());

            return Jwts.builder()
                    .setSubject(username)
                    .claim("role", "ROLE_" + role)
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                    .compact();

        } catch (Exception e) {
            System.out.println("❌ generateToken 예외: " + e.getClass().getSimpleName() + " / " + e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}

