package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.dto.CardapioResponse;
import br.com.fiap.techchallenge.dto.CreateCardapioRequest;
import br.com.fiap.techchallenge.dto.CreateItemCardapioRequest;
import br.com.fiap.techchallenge.service.CardapioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/restaurante/cardapio")
    public ResponseEntity criarCardapio(@Valid @RequestBody CreateCardapioRequest request){
        svc.criarCardapio(request);
        return ResponseEntity.ok("Cardápio criado com sucesso");
    }
    @PostMapping("/item-cardapio")
    public ResponseEntity criarItemCardapio(@Valid @RequestBody CreateItemCardapioRequest request){
        svc.criarItemCardapio(request);
        return ResponseEntity.ok("Item do cardapio criado com sucesso");
    }
    @DeleteMapping("/cardapio/{id}")
    public ResponseEntity deletarCardapio(@PathVariable Long id){
        svc.deletarCardapio(id);
        return ResponseEntity.ok("Item deletado com sucesso");
    }
    @DeleteMapping("/item-cardapio/{id}")
    public ResponseEntity deletarItemCardapio(@PathVariable Long id){
        svc.deletarItemCardapio(id);
        return ResponseEntity.ok("Item deletado com sucesso");
    }
}