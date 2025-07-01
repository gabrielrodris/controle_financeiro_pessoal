package com.example.controle_financeiro.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.controle_financeiro.dto.UsuarioRequestDTO;
import com.example.controle_financeiro.dto.UsuarioResponseDTO;
import com.example.controle_financeiro.entity.Usuario;
import com.example.controle_financeiro.repository.UsuarioRepo;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepo usuarioRepo;


    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        if (usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já registrado");
        }
        Usuario usuario = dto.toEntity();
        String hashedPassword = BCrypt.withDefaults().hashToString(12, dto.getSenha().toCharArray());
        usuario.setSenha(hashedPassword);
        return UsuarioResponseDTO.fromEntity(usuarioRepo.save(usuario));
    }


    public Optional<UsuarioResponseDTO> getById(Long id) {
        return usuarioRepo.findById(id)
                .map(UsuarioResponseDTO::fromEntity);
    }


    public List<UsuarioResponseDTO> getByEmail(String email){
        return usuarioRepo.findByEmail(email)
                .stream().map(UsuarioResponseDTO::fromEntity)
                .toList();
    }


    public List<UsuarioResponseDTO> getAll(){
        return usuarioRepo.findAll().stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }


    @Transactional
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já registrado");
        }
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        Optional.ofNullable(dto.getSenha())
                .filter(senha -> !senha.isBlank())
                .ifPresent(senha -> {
                    // Hash the new password using BCrypt
                    String hashedPassword = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
                    usuario.setSenha(hashedPassword);
                });
        return UsuarioResponseDTO.fromEntity(usuarioRepo.save(usuario));
    }


    public void delete(Long id) {
        if (!usuarioRepo.existsById(id)){
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        usuarioRepo.deleteById(id);
    }
}
