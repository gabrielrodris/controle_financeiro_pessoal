package com.example.controle_financeiro.dto;

import com.example.controle_financeiro.entity.Categoria;
import com.example.controle_financeiro.entity.Transacao;
import com.example.controle_financeiro.entity.Usuario;
import com.example.controle_financeiro.enums.TipoTransacao;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransacaoRequestDTO {
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "Data é obrigatória")
    @PastOrPresent(message = "Data não pode ser futura")
    private LocalDate data;

    @NotNull(message = "Tipo é obrigatório")
    private TipoTransacao tipo;

    @NotNull(message = "Usuario é obrigatório")
    private Long usuarioId;

    @NotNull(message = "Categoria é obrigatória")
    private Long categoriaId;

    public TransacaoRequestDTO(String descricao, BigDecimal valor, TipoTransacao tipo,
                               Long usuarioId, Long categoriaId) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = LocalDate.now();
        this.tipo = tipo;
        this.usuarioId = usuarioId;
        this.categoriaId = categoriaId;
    }

    public Transacao toEntity(Usuario usuario, Categoria categoria) {
        Transacao transacao = new Transacao();
        transacao.setDescricao(this.descricao);
        transacao.setValor(this.valor);
        transacao.setData(this.data);
        transacao.setTipo(this.tipo);
        transacao.setUsuario(usuario);
        transacao.setCategoria(categoria);
        return transacao;
    }
}
