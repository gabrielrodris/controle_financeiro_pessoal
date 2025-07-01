package com.example.controle_financeiro.dto;

import com.example.controle_financeiro.entity.Categoria;
import com.example.controle_financeiro.enums.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDTO {
    private Long id;
    private String nome;
    private TipoTransacao tipo;

    public CategoriaResponseDTO(Long id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public static CategoriaResponseDTO fromEntity(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getTipo()
        );
    }

}
