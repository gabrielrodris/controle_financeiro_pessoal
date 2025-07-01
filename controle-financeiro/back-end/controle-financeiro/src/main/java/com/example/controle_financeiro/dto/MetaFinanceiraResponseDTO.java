package com.example.controle_financeiro.dto;

import com.example.controle_financeiro.entity.MetaFinanceira;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaFinanceiraResponseDTO {

    private Long id;
    private String nome;
    private BigDecimal valorObjetivo;
    private LocalDate dataLimite;
    private Long usuarioId;
    private String usuarioNome;

    public MetaFinanceiraResponseDTO(Long id, String nome, BigDecimal valorObjetivo,
                                     LocalDate dataLimite, Long usuarioId) {
        this.id = id;
        this.nome = nome;
        this.valorObjetivo = valorObjetivo;
        this.dataLimite = dataLimite;
        this.usuarioId = usuarioId;
    }

    public static MetaFinanceiraResponseDTO fromEntity(MetaFinanceira meta) {
        return new MetaFinanceiraResponseDTO(
                meta.getId(),
                meta.getNome(),
                meta.getValorObjetivo(),
                meta.getDataLimite(),
                meta.getUsuario().getId(),
                meta.getUsuario().getNome()
        );
    }
}
