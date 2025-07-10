package br.com.fiap.techchallenge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/usuarios")
public class UsuarioController {

    @GetMapping
    public String aloMundo() {
        return "Olá, Mundo!";
    }
}
