package com.example.controle_financeiro.entity;

import com.example.controle_financeiro.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao",nullable = false)
    private TipoTransacao tipo;


    public Categoria(String nome, TipoTransacao tipo) {
    }
}
