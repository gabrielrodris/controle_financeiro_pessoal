package com.example.controle_financeiro.controller;

import com.example.controle_financeiro.dto.LoginDTO;
import com.example.controle_financeiro.dto.UsuarioRequestDTO;
import com.example.controle_financeiro.dto.UsuarioResponseDTO;
import com.example.controle_financeiro.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(
            @Valid @RequestBody UsuarioRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(
            @Valid @RequestBody LoginDTO dto) {

        return ResponseEntity.ok(service.login(dto));
    }
}
