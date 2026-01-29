package com.example.simlab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // Trata RecursoNaoEncontradoException → 404 Not Found
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("timestamp", LocalDateTime.now());
        erro.put("status", HttpStatus.NOT_FOUND.value());
        erro.put("error", "Not Found");
        erro.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // Trata DuplicadoException → 409 Conflict
    @ExceptionHandler(DuplicadoException.class)
    public ResponseEntity<Map<String, Object>> tratarDuplicado(DuplicadoException ex) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("timestamp", LocalDateTime.now());
        erro.put("status", HttpStatus.CONFLICT.value());
        erro.put("error", "Conflict");
        erro.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    // Trata validações (@Valid) → 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, Object> erro = new HashMap<>();
        Map<String, String> campos = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            campos.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        erro.put("timestamp", LocalDateTime.now());
        erro.put("status", HttpStatus.BAD_REQUEST.value());
        erro.put("error", "Bad Request");
        erro.put("message", "Erro de validação");
        erro.put("campos", campos);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Trata IllegalArgumentException (genérico) → 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("timestamp", LocalDateTime.now());
        erro.put("status", HttpStatus.BAD_REQUEST.value());
        erro.put("error", "Bad Request");
        erro.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
