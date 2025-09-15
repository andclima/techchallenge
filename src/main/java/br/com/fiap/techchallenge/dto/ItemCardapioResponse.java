package br.com.fiap.techchallenge.dto;

import java.math.BigDecimal;

public record ItemCardapioResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Boolean viagemSN,
        String caminhoFoto
) {
}
