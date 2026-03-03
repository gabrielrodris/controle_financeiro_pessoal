package com.example.controle_financeiro.controller;

import com.example.controle_financeiro.dto.LoginRequestDTO;
import com.example.controle_financeiro.dto.LoginResponseDTO;
import com.example.controle_financeiro.security.JwtService;
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
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                );

        authenticationManager.authenticate(authenticationToken);

        String token = jwtService.generateToken(request.getEmail());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
