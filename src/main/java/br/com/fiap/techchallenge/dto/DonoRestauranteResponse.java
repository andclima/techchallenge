package br.com.fiap.techchallenge.dto;

public record DonoRestauranteResponse(
  Long id,
  String nome,
  String email,
  String username,
  String endereco,
  String nomeDoRestaurante
) implements UsuarioResponseBase {}