package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.CreateRestauranteRequest;
import br.com.fiap.techchallenge.dto.RestauranteResponse;
import br.com.fiap.techchallenge.dto.UpdateRestauranteRequest;
import br.com.fiap.techchallenge.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class RestauranteController {

    private final RestauranteService svc;
    public RestauranteController(RestauranteService svc) {
        this.svc = svc;
    }
    @GetMapping("/restaurante")
    public ResponseEntity<List<RestauranteResponse>> listRestaurantes(){
        var listaDeRestaurantes = svc.buscarRestaurante();
        return ResponseEntity.ok(listaDeRestaurantes);
    }
    @GetMapping("/restaurante/{id}")
    public ResponseEntity<RestauranteResponse> getRestaurante(@PathVariable Long id){
        var restaurante = svc.buscarRestauranteId(id);
        return ResponseEntity.ok(restaurante);
    }
    @PostMapping("/restaurante")
    public ResponseEntity<RestauranteResponse> createRestaurante(@Valid @RequestBody CreateRestauranteRequest request){
        RestauranteResponse novoRestaurante = svc.criarRestaurante(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoRestaurante.id())
                .toUri();
        return ResponseEntity.created(location).body(novoRestaurante);
    }
    @PutMapping("/restaurante")
    public ResponseEntity<RestauranteResponse> updateRestaurante(@Valid @RequestBody UpdateRestauranteRequest request){
        RestauranteResponse restauranteAtualizado = svc.editarRestaurante(request);
        return ResponseEntity.ok(restauranteAtualizado);
    }
    @DeleteMapping("/restaurante/{id}")
    public ResponseEntity deleteRestaurante(@PathVariable Long id){
        svc.excluirRestaurante(id);
        return ResponseEntity.noContent().build();
    }
}