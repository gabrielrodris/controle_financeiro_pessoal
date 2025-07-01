package com.example.controle_financeiro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String senha;
    @Column(name = "data_cadastro",nullable = false,updatable = false)
    private LocalDateTime dataCadasto;

    @PrePersist
    protected void onCreate(){
        this.dataCadasto = LocalDateTime.now();
    }

    public void setDataCadasto(LocalDateTime dataCadasto) {
        this.dataCadasto = dataCadasto;
    }

    public void setDataCadastro(LocalDateTime now) {
    }



    public LocalDateTime getDataCadastro() {
        return dataCadasto;
    }
}
