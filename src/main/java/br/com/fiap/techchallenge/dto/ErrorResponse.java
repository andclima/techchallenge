package br.com.fiap.techchallenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        // Usaremos este campo para erros de validação
        Map<String, String> validationErrors
) {

    public ErrorResponse(Instant timestamp, Integer status, String error, String message, String path) {
        this(timestamp, status, error, message, path, null);
    }
}