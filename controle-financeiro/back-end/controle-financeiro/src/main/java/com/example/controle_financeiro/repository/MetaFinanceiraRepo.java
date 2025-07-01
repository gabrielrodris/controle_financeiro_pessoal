package com.example.controle_financeiro.repository;

import com.example.controle_financeiro.entity.MetaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MetaFinanceiraRepo extends JpaRepository<MetaFinanceira, Long> {
    List<MetaFinanceira> findByUsuarioId(Long usuarioId);
    List<MetaFinanceira> findByUsuarioIdAndDataLimiteAfter(Long usuarioId, LocalDate data);
}
