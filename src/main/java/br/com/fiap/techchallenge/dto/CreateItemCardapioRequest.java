package br.com.fiap.techchallenge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateItemCardapioRequest(
    @NotBlank(message = "O campo ao lado não pode estar vazio")
    String nomeItem,
    @NotBlank(message = "O campo ao lado não pode estar vazio")
    String descricao,
    @Positive(message = "Insira um valor positivo")
    @NotNull(message = "O campo não pode ser nulo")
    BigDecimal preco,
    @NotNull(message = "O campo não pode ser nulo")
    Boolean viagemSN,
    @NotBlank(message = "O campo ao lado não pode estar vazio")
    String caminhoFoto,
    @NotNull(message = "O campo não pode ser nulo")
    Long cardapio
) {
}
