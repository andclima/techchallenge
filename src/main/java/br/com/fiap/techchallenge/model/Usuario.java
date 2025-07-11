package br.com.fiap.techchallenge.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Usuario {
    private String nome;
    private String email;
    private String login;
    private String senha;
    private Date dataAtualizacao;
    private String endereco;

}
