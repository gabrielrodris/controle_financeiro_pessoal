package com.example.controle_financeiro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "meta_financeira")
public class MetaFinanceira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(name = "valor_objetivo",nullable = false)
    private BigDecimal valorObjetivo;
    @Column(name = "data_limite",nullable = false)
    private LocalDate dataLimite;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public MetaFinanceira(String nome, BigDecimal valorObjetivo, LocalDate dataLimite, Usuario usuario) {
    }
}
