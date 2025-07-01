package com.example.controle_financeiro.repository;

import com.example.controle_financeiro.entity.Transacao;
import com.example.controle_financeiro.enums.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepo extends JpaRepository<Transacao, Long> {

    List<Transacao> findByUsuarioId(Long usuarioId);
    List<Transacao> findByUsuarioIdAndTipo(Long usuarioId, TipoTransacao tipo);
    List<Transacao> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate inicio, LocalDate fim);
    List<Transacao> findByUsuarioIdAndCategoriaId(Long usuarioId, Long categoriaId);

    @Query("SELECT SUM(t.valor) FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.tipo = :tipo")
    BigDecimal calcularTotalPorTipo(Long usuarioId, TipoTransacao tipo);
}
