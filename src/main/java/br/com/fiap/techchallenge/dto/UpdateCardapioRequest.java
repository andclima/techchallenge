package br.com.fiap.techchallenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCardapioRequest(
        @NotNull(message = "O campo ao lado não pode estar vazio")
        Long id,
        @NotBlank(message = "O campo ao lado não pode estar vazio")
        String nomeCardapio
) {
}
