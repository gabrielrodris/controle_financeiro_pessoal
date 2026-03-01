package com.example.controle_financeiro.dto;

import com.example.controle_financeiro.entity.Categoria;
import com.example.controle_financeiro.enums.TipoTransacao;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoriaRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String nome;

    @NotNull(message = "Tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao")
    private TipoTransacao tipo;



    public Categoria toEntity(){
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setTipo(tipo);
        return categoria;
    }
}
