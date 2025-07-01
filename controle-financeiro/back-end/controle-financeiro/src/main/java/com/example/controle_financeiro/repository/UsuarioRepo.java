package com.example.controle_financeiro.repository;

import com.example.controle_financeiro.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Long> {

    boolean existsById(Long id);
    Optional<Usuario> findById(Long id);
    boolean existsByEmail(String email);
    List<Usuario> findByEmail(String email);
}
