package br.com.fiap.techchallenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCardapioRequest(
        @NotBlank(message = "O campo ao lado não pode estar vazio")
        String nomeCardapio,
        @NotNull(message = "O campo ao lado não pode estar vazio")
        Long idRestaurante
) {
}
