package com.example.controle_financeiro.controller;

import com.example.controle_financeiro.dto.LoginRequestDTO;
import com.example.controle_financeiro.dto.LoginResponseDTO;
import com.example.controle_financeiro.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                );

        authenticationManager.authenticate(authenticationToken);

        return ResponseEntity.ok("Login realizado com sucesso");
    }
}
