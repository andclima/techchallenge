package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.CreateRestauranteRequest;
import br.com.fiap.techchallenge.dto.RestauranteResponse;
import br.com.fiap.techchallenge.dto.UpdateRestauranteRequest;
import br.com.fiap.techchallenge.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
public class RestauranteController {

    private final RestauranteService svc;
    public RestauranteController(RestauranteService svc) {
        this.svc = svc;
    }
    @GetMapping("/restaurantes")
    public ResponseEntity<List<RestauranteResponse>> listRestaurantes(){
        var listaDeRestaurantes = svc.buscarRestaurante();
        return ResponseEntity.ok(listaDeRestaurantes);
    }
    @PostMapping("/restaurante")
    public ResponseEntity createRestaurante(@Valid @RequestBody CreateRestauranteRequest request){
        svc.criarRestaurante(request);
        return ResponseEntity.ok("Restaurante criado com sucesso");
    }
    @PutMapping("/restaurante")
    public ResponseEntity updateRestaurante(@Valid @RequestBody UpdateRestauranteRequest request){
        svc.editarRestaurante(request);
        return ResponseEntity.ok("Restaurante editado com sucesso");
    }
}