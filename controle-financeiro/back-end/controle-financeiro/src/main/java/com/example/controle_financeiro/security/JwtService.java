package com.example.controle_financeiro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;


@Service
public class JwtService {

    // chave secreta
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 1 hora
    private final long EXPIRATION_TIME = 1000 * 60 * 60;
    // gerar token
    public String generateToken(String email) {

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
        return (username.equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }
}
