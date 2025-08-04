package br.com.fiap.techchallenge.dto;

public record ClienteResponse(
  Long id,
  String nome,
  String email,
  String username,
  String endereco,
  String numeroFidelidade,
  String tipo
) implements UsuarioResponseBase {
    @Override
    public Long getId() {
        return this.id;
    }
}