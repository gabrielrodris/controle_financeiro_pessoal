package com.example.controle_financeiro.dto;

import com.example.controle_financeiro.entity.MetaFinanceira;
import com.example.controle_financeiro.entity.Usuario;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MetaFinanceiraRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String nome;

    @NotNull(message = "Valor objetivo é obrigatório")
    @Positive(message = "Valor objetivo deve ser positivo")
    @DecimalMin(value = "0.01", message = "Valor objetivo deve ser maior que zero")
    private BigDecimal valorObjetivo;

    @NotNull(message = "Data limite é obrigatória")
    @FutureOrPresent(message = "Data limite deve ser hoje ou futura")
    private LocalDate dataLimite;

    @NotNull(message = "Usuário é obrigatório")
    private Long usuarioId;


    public MetaFinanceira toEntity(Usuario usuario){
        MetaFinanceira meta = new MetaFinanceira();
        meta.setNome(nome);
        meta.setValorObjetivo(valorObjetivo);
        meta.setDataLimite(dataLimite);
        meta.setUsuario(usuario);
        return meta;
    }
}
