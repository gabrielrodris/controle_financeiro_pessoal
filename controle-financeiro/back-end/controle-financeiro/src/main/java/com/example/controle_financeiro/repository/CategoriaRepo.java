package com.example.controle_financeiro.repository;

import com.example.controle_financeiro.entity.Categoria;
import com.example.controle_financeiro.enums.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepo extends JpaRepository<Categoria, Long> {

    boolean existsByNomeAndTipo(String nome, TipoTransacao tipo);
    Optional<Categoria> findByNomeAndTipo(String nome, TipoTransacao tipo);
    List<Categoria> findByTipo(TipoTransacao tipo);
    boolean existsById(Long id);
    Optional<Categoria> findById(Long id);

}
