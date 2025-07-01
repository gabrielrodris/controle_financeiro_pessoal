package com.example.controle_financeiro.controller;

import com.example.controle_financeiro.enums.TipoTransacao;
import com.example.controle_financeiro.errorMessage.ErrorResponse;
import com.example.controle_financeiro.dto.CategoriaRequestDTO;
import com.example.controle_financeiro.dto.CategoriaResponseDTO;
import com.example.controle_financeiro.service.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> create(@Valid @RequestBody CategoriaRequestDTO categoriaRequestDTO){
        try{
            CategoriaResponseDTO categoriaReponse = categoriaService.create(categoriaRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaReponse);
        }catch (IllegalArgumentException erro){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> getById(@PathVariable Long id) {
        return categoriaService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> getAll() {
        List<CategoriaResponseDTO> categorias = categoriaService.getAll();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CategoriaResponseDTO>> getByTipo(@PathVariable String tipo) {
        try {
            TipoTransacao tipoTransacao = Enum.valueOf(TipoTransacao.class, tipo.toUpperCase());
            return ResponseEntity.ok(categoriaService.getByTipo(tipoTransacao));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoriaRequestDTO dto) {
        try {
            CategoriaResponseDTO response = categoriaService.update(id, dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            categoriaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }



}
