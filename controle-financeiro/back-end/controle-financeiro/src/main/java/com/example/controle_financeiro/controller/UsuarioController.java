package com.example.controle_financeiro.controller;

import com.example.controle_financeiro.errorMessage.ErrorResponse;
import com.example.controle_financeiro.dto.UsuarioRequestDTO;
import com.example.controle_financeiro.dto.UsuarioResponseDTO;
import com.example.controle_financeiro.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {



    @Autowired
    private UsuarioService usuarioService;



    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid UsuarioRequestDTO dto) {
        try {
            UsuarioResponseDTO response = usuarioService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id) {
        return usuarioService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/email")
    public ResponseEntity<UsuarioResponseDTO> getByEmail(@RequestParam String email) {
        try {
            UsuarioResponseDTO usuario = usuarioService.getByEmail(email);
            return ResponseEntity.ok(usuario);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> getMe(){

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        UsuarioResponseDTO usuario = usuarioService.getByEmail(email);

        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        System.out.println("Usuario logado: " + email);
        List<UsuarioResponseDTO> usuarios = usuarioService.getAll();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) {
        try {
            UsuarioResponseDTO response = usuarioService.update(id, dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

