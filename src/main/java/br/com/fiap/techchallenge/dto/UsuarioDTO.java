package br.com.fiap.techchallenge.dto;

import lombok.Data;

@Data
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String username;
    private String password;
    private String endereco;

}
