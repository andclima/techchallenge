package br.com.fiap.techchallenge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUsuarioRequest(

        @NotBlank(message = "O nome não pode ser vazio.")
        String nome,

        @Email(message = "O formato do e-mail é inválido.")
        @NotBlank(message = "O e-mail não pode ser vazio.")
        String email,

        @NotBlank(message = "O username não pode ser vazio.")
        String username,

        @NotBlank(message = "A senha não pode ser vazia.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String password,

        String endereco,

        @NotBlank(message = "O tipo de usuário não pode ser vazio.")
        String tipo,

        String nomeDoRestaurante,
        String numeroFidelidade
) {
}
