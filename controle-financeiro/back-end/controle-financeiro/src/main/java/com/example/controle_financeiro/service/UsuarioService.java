package com.example.controle_financeiro.service;

import com.example.controle_financeiro.dto.LoginRequestDTO;
import com.example.controle_financeiro.dto.LoginResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.controle_financeiro.dto.UsuarioRequestDTO;
import com.example.controle_financeiro.dto.UsuarioResponseDTO;
import com.example.controle_financeiro.entity.Usuario;
import com.example.controle_financeiro.repository.UsuarioRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepo usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepo usuarioRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public UsuarioResponseDTO create(UsuarioRequestDTO dto) {
        if (usuarioRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já registrado");
        }

        Usuario usuario = dto.toEntity();

        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        return UsuarioResponseDTO.fromEntity(usuarioRepo.save(usuario));
    }


    public Optional<UsuarioResponseDTO> getById(Long id) {
        return usuarioRepo.findById(id)
                .map(UsuarioResponseDTO::fromEntity);
    }


    public UsuarioResponseDTO getByEmail(String email){
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return new UsuarioResponseDTO(usuario);
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
                .ifPresent(senha ->
                        usuario.setSenha(passwordEncoder.encode(senha))
                );
        return UsuarioResponseDTO.fromEntity(usuarioRepo.save(usuario));
    }


    public void delete(Long id) {
        if (!usuarioRepo.existsById(id)){
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        usuarioRepo.deleteById(id);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Senha inválida");
        }

        return new LoginResponseDTO("Login realizado com sucesso");
    }
}
