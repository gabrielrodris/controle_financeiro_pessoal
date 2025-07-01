package com.example.controle_financeiro.service;

import com.example.controle_financeiro.dto.MetaFinanceiraRequestDTO;
import com.example.controle_financeiro.dto.MetaFinanceiraResponseDTO;
import com.example.controle_financeiro.entity.MetaFinanceira;
import com.example.controle_financeiro.entity.Usuario;
import com.example.controle_financeiro.repository.MetaFinanceiraRepo;
import com.example.controle_financeiro.repository.UsuarioRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MetaFinanceiraService {

    @Autowired
    private MetaFinanceiraRepo metaFinanceiraRepo;
    @Autowired
    private UsuarioRepo usuarioRepo;

    @Transactional
    public MetaFinanceiraResponseDTO create(MetaFinanceiraRequestDTO dto) {
        // Validate usuarioId and fetch Usuario
        Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        // Check for duplicate goal
        if (metaFinanceiraRepo.findByUsuarioIdAndDataLimiteAfter(dto.getUsuarioId(), LocalDate.now())
                .stream()
                .anyMatch(meta -> meta.getNome().equals(dto.getNome()))) {
            throw new IllegalArgumentException("Meta financeira com mesmo nome já registrada para este usuário");
        }

        MetaFinanceira meta = dto.toEntity(usuario);
        return MetaFinanceiraResponseDTO.fromEntity(metaFinanceiraRepo.save(meta));
    }

    public Optional<MetaFinanceiraResponseDTO> getById(Long id) {
        return metaFinanceiraRepo.findById(id)
                .map(MetaFinanceiraResponseDTO::fromEntity);
    }

    public List<MetaFinanceiraResponseDTO> getByUsuarioId(Long usuarioId) {
        // Validate usuarioId
        if (!usuarioRepo.existsById(usuarioId)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        return metaFinanceiraRepo.findByUsuarioId(usuarioId)
                .stream()
                .map(MetaFinanceiraResponseDTO::fromEntity)
                .toList();
    }

    public List<MetaFinanceiraResponseDTO> getByUsuarioIdAndDataLimiteAfter(Long usuarioId, LocalDate data) {
        // Validate usuarioId
        if (!usuarioRepo.existsById(usuarioId)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        return metaFinanceiraRepo.findByUsuarioIdAndDataLimiteAfter(usuarioId, data)
                .stream()
                .map(MetaFinanceiraResponseDTO::fromEntity)
                .toList();
    }

    public List<MetaFinanceiraResponseDTO> getAll() {
        return metaFinanceiraRepo.findAll()
                .stream()
                .map(MetaFinanceiraResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public MetaFinanceiraResponseDTO update(Long id, MetaFinanceiraRequestDTO dto) {
        // Validate usuarioId and fetch Usuario
        Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        MetaFinanceira meta = metaFinanceiraRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meta financeira não encontrada"));

        // Check for duplicate goal
        if (!meta.getNome().equals(dto.getNome()) || !meta.getUsuario().getId().equals(dto.getUsuarioId())) {
            if (metaFinanceiraRepo.findByUsuarioIdAndDataLimiteAfter(dto.getUsuarioId(), LocalDate.now())
                    .stream()
                    .anyMatch(m -> m.getNome().equals(dto.getNome()) && !m.getId().equals(id))) {
                throw new IllegalArgumentException("Meta financeira com mesmo nome já registrada para este usuário");
            }
        }

        meta.setNome(dto.getNome());
        meta.setValorObjetivo(dto.getValorObjetivo());
        meta.setDataLimite(dto.getDataLimite());
        meta.setUsuario(usuario);
        return MetaFinanceiraResponseDTO.fromEntity(metaFinanceiraRepo.save(meta));
    }

    @Transactional
    public void delete(Long id) {
        if (!metaFinanceiraRepo.existsById(id)) {
            throw new EntityNotFoundException("Meta financeira não encontrada");
        }
        metaFinanceiraRepo.deleteById(id);
    }


}
