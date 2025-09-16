package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.*;
import br.com.fiap.techchallenge.service.CardapioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class CardapioController {

    private final CardapioService svc;

    public CardapioController(CardapioService cardapioService) {
        this.svc = cardapioService;
    }
    @GetMapping("/restaurante/{idRestaurante}/cardapio")
    public ResponseEntity<CardapioResponse> buscarCardapioPorRestaurante(@PathVariable Long idRestaurante){
        var cardapio = svc.buscarCardapioDoRestaurante(idRestaurante);
        return ResponseEntity.ok(cardapio);
    }
    @GetMapping("item-cardapio/{id}")
    public ResponseEntity<ItemCardapioResponse> buscarItemCardapio(@PathVariable Long id){
        var itemCardapio = svc.buscarItemCardapio(id);
        return ResponseEntity.ok(itemCardapio);
    }
    @GetMapping("/cardapios")
    public ResponseEntity<List<CardapioResponse>> listarCardapios(){
        List<CardapioResponse> cardapios = svc.listarCardapios();
        return ResponseEntity.ok().body(cardapios);
    }
    @PostMapping("/restaurante/cardapio")
    public ResponseEntity<CardapioResponse> criarCardapio(@Valid @RequestBody CreateCardapioRequest request){
        CardapioResponse novoCardapio = svc.criarCardapio(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoCardapio.id())
                .toUri();
        return ResponseEntity.created(location).body(novoCardapio);
    }
    @PostMapping("/item-cardapio")
    public ResponseEntity<ItemCardapioResponse> criarItemCardapio(@Valid @RequestBody CreateItemCardapioRequest request){
        ItemCardapioResponse novoItemCardapio = svc.criarItemCardapio(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoItemCardapio.id())
                .toUri();
        return ResponseEntity.created(location).body(novoItemCardapio);
    }
    @PutMapping("/restaurante/cardapio")
    public ResponseEntity atualizarCardapio(@Valid @RequestBody UpdateCardapioRequest request){
        CardapioResponse novoCardapio = svc.updateCardapio(request);
        return ResponseEntity.ok(novoCardapio);
    }
    @PutMapping("/item-cardapio")
    public ResponseEntity atualizarItemCardapio(@Valid @RequestBody UpdateItemCardapioRequest request){
        ItemCardapioResponse novoItem = svc.updateItemCardapio(request);
        return ResponseEntity.ok(novoItem);
    }
    @DeleteMapping("/cardapio/{id}")
    public ResponseEntity deletarCardapio(@PathVariable Long id){
        svc.deletarCardapio(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/item-cardapio/{id}")
    public ResponseEntity deletarItemCardapio(@PathVariable Long id){
        svc.deletarItemCardapio(id);
        return ResponseEntity.noContent().build();
    }
}