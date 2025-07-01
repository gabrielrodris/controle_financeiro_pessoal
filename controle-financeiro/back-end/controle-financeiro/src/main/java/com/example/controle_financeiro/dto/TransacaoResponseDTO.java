package com.example.controle_financeiro.dto;

import com.example.controle_financeiro.entity.Transacao;
import com.example.controle_financeiro.enums.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoResponseDTO {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private TipoTransacao tipo;
    private Long usuarioId;
    private String usuarioNome;
    private Long categoriaId;
    private String categoriaNome;

    public TransacaoResponseDTO(Long id, String descricao, BigDecimal valor, LocalDate data,
                                TipoTransacao tipo, Long usuarioId, Long categoriaId) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.tipo = tipo;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
    }

    public TransacaoResponseDTO(Transacao transacao) {
        this.id = transacao.getId();
        this.descricao = transacao.getDescricao();
        this.valor = transacao.getValor();
        this.data = transacao.getData();
        this.tipo = transacao.getTipo();
        this.usuarioId = transacao.getUsuario().getId();
        this.usuarioNome = transacao.getUsuario().getNome();
        this.categoriaId = transacao.getCategoria().getId();
        this.categoriaNome = transacao.getCategoria().getNome();
    }

    public static TransacaoResponseDTO fromEntity(Transacao transacao){
        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getData(),
                transacao.getTipo(),
                transacao.getUsuario().getId(),
                transacao.getUsuario().getNome(),
                transacao.getCategoria().getId(),
                transacao.getCategoria().getNome()
        );
    }

}
