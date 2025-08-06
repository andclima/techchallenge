package br.com.fiap.techchallenge.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

public record UpdateUsuarioRequest(
        @NotNull(message = "O ID do usuário não pode ser nulo.")
        Long id,
        String nome,
        @Email(message = "O formato do e-mail é inválido se fornecido.")
        String email,
        String endereco,
        String nomeDoRestaurante,
        String numeroFidelidade
) {
}
