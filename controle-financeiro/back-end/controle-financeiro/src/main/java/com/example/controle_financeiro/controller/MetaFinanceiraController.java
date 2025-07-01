package com.example.controle_financeiro.controller;

import com.example.controle_financeiro.dto.MetaFinanceiraRequestDTO;
import com.example.controle_financeiro.dto.MetaFinanceiraResponseDTO;
import com.example.controle_financeiro.service.MetaFinanceiraService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/metaFinanceiras")
@Validated
public class MetaFinanceiraController {

    @Autowired
    private MetaFinanceiraService metaFinanceiraService;

    @PostMapping
    public ResponseEntity<MetaFinanceiraResponseDTO> create(@Valid @RequestBody MetaFinanceiraRequestDTO dto) {
        MetaFinanceiraResponseDTO response = metaFinanceiraService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaFinanceiraResponseDTO> getById(@PathVariable Long id) {
        return metaFinanceiraService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<MetaFinanceiraResponseDTO>> getByUsuarioId(@PathVariable Long usuarioId){
        try {
            return ResponseEntity.ok(metaFinanceiraService.getByUsuarioId(usuarioId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/usuario/{usuarioId}/ativas")
    public ResponseEntity<List<MetaFinanceiraResponseDTO>> getByUsuarioIdAndDataLimiteAfter(@PathVariable Long usuarioId, @RequestParam(name = "data") String data){
        try {
            LocalDate localDate = LocalDate.parse(data);
            return ResponseEntity.ok(metaFinanceiraService.getByUsuarioIdAndDataLimiteAfter(usuarioId, localDate));
        } catch (EntityNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<MetaFinanceiraResponseDTO>> getAll(){
        return ResponseEntity.ok(metaFinanceiraService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaFinanceiraResponseDTO> update(@PathVariable Long id, @Valid @RequestBody MetaFinanceiraRequestDTO dto){
        return ResponseEntity.ok(metaFinanceiraService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        try {
            metaFinanceiraService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
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
                    .body("Data limite inválida. Use o formato AAAA-MM-DD (ex.: 2025-12-31).");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato inválido: " + ex.getMessage());
    }
}
