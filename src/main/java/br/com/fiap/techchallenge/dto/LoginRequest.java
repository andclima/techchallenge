package br.com.fiap.techchallenge.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "O nome de usuário não pode ser vazio")
        String username,

        @NotBlank(message = "A senha não pode ser vazia")
        String password
) {}
