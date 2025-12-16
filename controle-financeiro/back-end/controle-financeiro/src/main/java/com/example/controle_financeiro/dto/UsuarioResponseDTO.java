package com.example.controle_financeiro.dto;

import com.example.controle_financeiro.entity.Usuario;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private LocalDateTime dataCadastro;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.dataCadastro = usuario.getDataCadastro();
    }
}
