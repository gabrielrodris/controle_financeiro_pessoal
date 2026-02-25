package com.example.controle_financeiro.controller;

import com.example.controle_financeiro.dto.LoginRequestDTO;
import com.example.controle_financeiro.dto.LoginResponseDTO;
import com.example.controle_financeiro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO dto) {
        return usuarioService.login(dto);
    }
}
