package br.com.fiap.techchallenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordRequest(
        @NotBlank(message = "O username não pode ser vazio.")
        String username,

        @NotBlank(message = "A senha atual não pode ser vazia.")
        String password,

        @NotBlank(message = "A nova senha não pode ser vazia.")
        @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres.")
        String newPassword
) {
}
