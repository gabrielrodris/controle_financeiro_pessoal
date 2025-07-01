package com.example.controle_financeiro.service;

import com.example.controle_financeiro.dto.TransacaoRequestDTO;
import com.example.controle_financeiro.dto.TransacaoResponseDTO;
import com.example.controle_financeiro.entity.Categoria;
import com.example.controle_financeiro.entity.Transacao;
import com.example.controle_financeiro.entity.Usuario;
import com.example.controle_financeiro.enums.TipoTransacao;
import com.example.controle_financeiro.repository.CategoriaRepo;
import com.example.controle_financeiro.repository.TransacaoRepo;
import com.example.controle_financeiro.repository.UsuarioRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepo transacaoRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private CategoriaRepo categoriaRepo;

    @Transactional
    public TransacaoResponseDTO create(TransacaoRequestDTO dto){
        Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(()  -> new EntityNotFoundException("Usuário não encontrado"));
        Categoria categoria = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        if (!categoria.getTipo().equals(dto.getTipo())){
            throw new IllegalArgumentException("O tipo de transação deve corresponder ao tipo da categoria");
        }

        Transacao transacao = dto.toEntity(usuario, categoria);
        return TransacaoResponseDTO.fromEntity(transacaoRepo.save(transacao));

    }

    public Optional<TransacaoResponseDTO> getById(Long id){
        return transacaoRepo.findById(id)
                .map(TransacaoResponseDTO::fromEntity);
    }

    public List<TransacaoResponseDTO> getByUsuarioId(Long usuarioId){
        if (!usuarioRepo.existsById(usuarioId)){
            throw new EntityNotFoundException("Usuario não encontrado");
        }
        return transacaoRepo.findByUsuarioId(usuarioId)
                .stream()
                .map(TransacaoResponseDTO::fromEntity)
                .toList();
    }

    public List<TransacaoResponseDTO> getByUsuarioIdAndTipo(Long usuarioId, TipoTransacao tipo){
        if (!usuarioRepo.existsById(usuarioId)){
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        return transacaoRepo.findByUsuarioIdAndTipo(usuarioId, tipo)
                .stream()
                .map(TransacaoResponseDTO::fromEntity)
                .toList();
    }


    public List<TransacaoResponseDTO> getByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim) {
        if (!usuarioRepo.existsById(usuarioId)){
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        if (inicio.isAfter(fim)){
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim");
        }
        return transacaoRepo.findByUsuarioIdAndDataBetween(usuarioId, inicio, fim)
                .stream()
                .map(TransacaoResponseDTO::fromEntity)
                .toList();
    }

    public List<TransacaoResponseDTO> getByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId){
        if (!usuarioRepo.existsById(usuarioId)){
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        if (!categoriaRepo.existsById(categoriaId)){
            throw new EntityNotFoundException("Categoria não encontrada");
        }
        return transacaoRepo.findByUsuarioIdAndCategoriaId(usuarioId, categoriaId)
                .stream()
                .map(TransacaoResponseDTO::fromEntity)
                .toList();
    }

    public BigDecimal calcularTotalPorTipo(Long usuarioId, TipoTransacao tipo){
        if (!usuarioRepo.existsById(usuarioId)){
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        BigDecimal total = transacaoRepo.calcularTotalPorTipo(usuarioId, tipo);
        return total !=null ? total : BigDecimal.ZERO;
    }

    public List<TransacaoResponseDTO> getAll(){
        return transacaoRepo.findAll()
                .stream()
                .map(TransacaoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional
    public TransacaoResponseDTO update(Long id, TransacaoRequestDTO dto){
        Transacao transacao = transacaoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada"));

        Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Categoria categoria = categoriaRepo.findById(dto.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));

        transacao.setDescricao(dto.getDescricao());
        transacao.setValor(dto.getValor());
        transacao.setData(dto.getData());
        transacao.setTipo(dto.getTipo());
        transacao.setUsuario(usuario);
        transacao.setCategoria(categoria);
        return TransacaoResponseDTO.fromEntity(transacaoRepo.save(transacao));
    }

    @Transactional
    public void delete(Long id){
        if (!transacaoRepo.existsById(id)){
            throw new EntityNotFoundException("Transação não encontrada");
        }
        transacaoRepo.deleteById(id);
    }

}
