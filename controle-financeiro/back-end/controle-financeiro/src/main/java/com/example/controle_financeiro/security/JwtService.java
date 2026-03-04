package com.example.controle_financeiro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Slf4j
@Service
public class JwtService {

    private static final String SECRET = "minha-chave-super-secreta-para-jwt-2026-abcdef";
    // chave secreta
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    // 1 hora
    private final long EXPIRATION_TIME = 1000 * 60 * 60;
    // gerar token
    public String generateToken(String email) {

        System.out.println("GERANDO TOKEN COM SECRET: " + SECRET);

        return Jwts.builder()
                .setSubject(email) // quem é o usuario
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();

    }

    // estrair email do token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // validar token
    public boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token);
        System.out.println("Token username: " + username);
        System.out.println("UserDetails username: " + email);
        return (username.equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        System.out.println("VALIDANDO TOKEN COM SECRET: " + SECRET);

        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }


}
