package com.example.controle_financeiro.service;

import com.example.controle_financeiro.dto.LoginDTO;
import com.example.controle_financeiro.dto.UsuarioRequestDTO;
import com.example.controle_financeiro.dto.UsuarioResponseDTO;
import com.example.controle_financeiro.entity.Usuario;
import com.example.controle_financeiro.repository.UsuarioRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepo repository;
    private final PasswordEncoder encoder;

    public AuthService(UsuarioRepo repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional
    public UsuarioResponseDTO register(UsuarioRequestDTO dto) {

        if (repository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(encoder.encode(dto.getSenha()));

        repository.save(usuario);

        return new UsuarioResponseDTO(usuario);
    }

    public UsuarioResponseDTO login(LoginDTO dto) {

        Usuario usuario = repository.findByEmail(dto.email())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (!encoder.matches(dto.senha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Senha inválida");
        }

        return new UsuarioResponseDTO(usuario);
    }
}
