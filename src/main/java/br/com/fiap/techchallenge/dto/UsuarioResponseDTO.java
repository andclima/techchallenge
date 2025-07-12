package br.com.fiap.techchallenge.dto;

import br.com.fiap.techchallenge.model.Usuario;
import lombok.Data;

import java.util.Date;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String username;
    private String endereco;
    private Date dataAtualizacao;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.username = usuario.getUsername();
        this.endereco = usuario.getEndereco();
        this.dataAtualizacao = usuario.getDataAtualizacao();
    }
}
