package br.com.fiap.techchallenge.dto;

public record ClienteResponse(
  Long id,
  String nome,
  String email,
  String username,
  String endereco,
  String numeroFidelidade
) implements UsuarioResponseBase {}