package br.com.fiap.techchallenge.dto;

import br.com.fiap.techchallenge.model.DonoRestaurante;
//import br.com.fiap.techchallenge.model.TipoCozinha;


public record RestauranteResponse(
         Long id,
         String nome,
         String endereco,
         String cep,
         String tipoCozinha,
         String horaFuncionamento,
         String contato,
         String Nomedono,
         String Emaildono
) {
}
