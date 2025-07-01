package com.example.controle_financeiro.controller;

import com.example.controle_financeiro.dto.TransacaoRequestDTO;
import com.example.controle_financeiro.dto.TransacaoResponseDTO;
import com.example.controle_financeiro.enums.TipoTransacao;
import com.example.controle_financeiro.service.TransacaoService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transacoes")
@Validated
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> create(@Valid @RequestBody TransacaoRequestDTO dto) {
        TransacaoResponseDTO response = transacaoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> getById(@PathVariable Long id) {
        return transacaoService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TransacaoResponseDTO>> getByUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(transacaoService.getByUsuarioId(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/tipo/{tipo}")
    public ResponseEntity<List<TransacaoResponseDTO>> getByUsuarioIdAndTipo(
            @PathVariable Long usuarioId,
            @PathVariable String tipo) {
        TipoTransacao tipoTransacao = TipoTransacao.valueOf(tipo.toUpperCase());
        return ResponseEntity.ok(transacaoService.getByUsuarioIdAndTipo(usuarioId, tipoTransacao));
    }

    @GetMapping("/usuario/{usuarioId}/data")
    public ResponseEntity<List<TransacaoResponseDTO>> getByUsuarioIdAndDataBetween(
            @PathVariable Long usuarioId,
            @RequestParam(name = "inicio") String inicio,
            @RequestParam(name = "fim") String fim) {
        LocalDate dataInicio = LocalDate.parse(inicio);
        LocalDate dataFim = LocalDate.parse(fim);
        return ResponseEntity.ok(transacaoService.getByUsuarioIdAndDataBetween(usuarioId, dataInicio, dataFim));
    }

    @GetMapping("/usuario/{usuarioId}/categoria/{categoriaId}")
    public ResponseEntity<List<TransacaoResponseDTO>> getByUsuarioIdAndCategoriaId(
            @PathVariable Long usuarioId,
            @PathVariable Long categoriaId) {
        return ResponseEntity.ok(transacaoService.getByUsuarioIdAndCategoriaId(usuarioId, categoriaId));
    }

    @GetMapping("/usuario/{usuarioId}/total/{tipo}")
    public ResponseEntity<BigDecimal> calcularTotalPorTipo(
            @PathVariable Long usuarioId,
            @PathVariable String tipo) {
        TipoTransacao tipoTransacao = TipoTransacao.valueOf(tipo.toUpperCase());
        return ResponseEntity.ok(transacaoService.calcularTotalPorTipo(usuarioId, tipoTransacao));
    }

    @GetMapping
    public ResponseEntity<List<TransacaoResponseDTO>> getAll() {
        return ResponseEntity.ok(transacaoService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TransacaoRequestDTO dto) {
        return ResponseEntity.ok(transacaoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<List<TransacaoResponseDTO>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handleInvalidFormatException(InvalidFormatException ex) {
        if (ex.getTargetType().equals(LocalDate.class)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data inválida. Use o formato AAAA-MM-DD (ex.: 2025-12-31). Valor recebido: " + ex.getValue());
        } else if (ex.getTargetType().equals(TipoTransacao.class)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tipo inválido. Use ENTRADA ou SAIDA. Valor recebido: " + ex.getValue());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Formato inválido para o campo " + ex.getPath().get(0).getFieldName() + ": " + ex.getValue());
    }
}
