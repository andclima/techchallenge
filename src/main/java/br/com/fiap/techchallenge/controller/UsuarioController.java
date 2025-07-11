package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.model.Usuario;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/usuarios")
public class UsuarioController {

    @PostMapping
    public String create() {
        return "Criado com sucesso!";
    }

    @GetMapping("/{id}")
    public String get(@PathParam("id") String id) {
        return "Usuario retornado com sucesso!";
    }

    @GetMapping
    public String list() {
        return "Lista de usuários retornada com sucesso!";
    }

    @PutMapping
    public String update() {
        return "Atualizado com sucesso!";
    }

    @DeleteMapping
    public String delete() {
        return "Removido com sucesso!";
    }

    @PostMapping("/auth")
    public String login() {
        return "Usuário logado com sucesso!";
    }

    public String alterarSenha() {
        return "Senha alterada com sucesso!";
    }
}
