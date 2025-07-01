package com.example.controle_financeiro.service;

import com.example.controle_financeiro.dto.CategoriaRequestDTO;
import com.example.controle_financeiro.dto.CategoriaResponseDTO;
import com.example.controle_financeiro.entity.Categoria;
import com.example.controle_financeiro.enums.TipoTransacao;
import com.example.controle_financeiro.repository.CategoriaRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Transactional
    public CategoriaResponseDTO create(CategoriaRequestDTO dto) {
        if (categoriaRepo.existsByNomeAndTipo(dto.getNome(), dto.getTipo())) {
            throw new IllegalArgumentException("Categoria com mesmo nome e tipo já registrada");
        }
        Categoria categoria = dto.toEntity();
        return CategoriaResponseDTO.fromEntity(categoriaRepo.save(categoria));
    }

    public Optional<CategoriaResponseDTO> getById(Long id) {
        return categoriaRepo.findById(id)
                .map(CategoriaResponseDTO::fromEntity);
    }

    public List<CategoriaResponseDTO> getAll() {
        return categoriaRepo.findAll().stream()
                .map(CategoriaResponseDTO::fromEntity)
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
