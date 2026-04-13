package com.example.controle_financeiro.service;

import com.example.controle_financeiro.dto.CategoriaRequestDTO;
import com.example.controle_financeiro.dto.CategoriaResponseDTO;
import com.example.controle_financeiro.dto.UsuarioResponseDTO;
import com.example.controle_financeiro.entity.Categoria;
import com.example.controle_financeiro.entity.Usuario;
import com.example.controle_financeiro.enums.TipoTransacao;
import com.example.controle_financeiro.repository.CategoriaRepo;
import com.example.controle_financeiro.repository.UsuarioRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class CategoriaService {


    private final CategoriaRepo categoriaRepo;
    private final UsuarioRepo usuarioRepo;

    @Transactional
    public CategoriaResponseDTO create(CategoriaRequestDTO dto, String email) {

        if (categoriaRepo.existsByNomeAndTipo(dto.getNome(), dto.getTipo())) {
            throw new IllegalArgumentException("Categoria com mesmo nome e tipo já registrada");
        }

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Categoria categoria = dto.toEntity();
        categoria.setUsuario(usuario);

        Categoria salva = categoriaRepo.save(categoria);

        return CategoriaResponseDTO.fromEntity(salva);
    }

    public Optional<CategoriaResponseDTO> getById(Long id) {
        return categoriaRepo.findById(id)
                .map(CategoriaResponseDTO::fromEntity);
    }

    public UsuarioResponseDTO getByEmail(String email) {

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new UsuarioResponseDTO(usuario);
    }

    public List<CategoriaResponseDTO> getAll(String email) {
        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return categoriaRepo.findByUsuario(usuario)
                .stream()
                .map(CategoriaResponseDTO::new)
                .toList();
    }

    public List<CategoriaResponseDTO> getByTipo(TipoTransacao tipo) {
        return categoriaRepo.findByTipo(tipo)
                .stream()
                .map(CategoriaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public CategoriaResponseDTO update(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
        if (!categoria.getNome().equals(dto.getNome()) || !categoria.getTipo().equals(dto.getTipo())) {
            if (categoriaRepo.existsByNomeAndTipo(dto.getNome(), dto.getTipo())) {
                throw new IllegalArgumentException("Categoria com mesmo nome e tipo já registrada");
            }
        }
        categoria.setNome(dto.getNome());
        categoria.setTipo(dto.getTipo());
        return CategoriaResponseDTO.fromEntity(categoriaRepo.save(categoria));
    }

    @Transactional
    public void delete(Long id) {
        if (!categoriaRepo.existsById(id)) {
            throw new EntityNotFoundException("Categoria não encontrada");
        }
        categoriaRepo.deleteById(id);
    }

}
