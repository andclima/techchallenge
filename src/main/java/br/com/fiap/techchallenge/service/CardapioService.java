package br.com.fiap.techchallenge.service;

import br.com.fiap.techchallenge.dto.*;
import br.com.fiap.techchallenge.exception.DadoDuplicadoException;
import br.com.fiap.techchallenge.exception.RecursoNaoEncontradoException;
import br.com.fiap.techchallenge.model.Cardapio;
import br.com.fiap.techchallenge.model.ItemCardapio;
import br.com.fiap.techchallenge.model.Restaurante;
import br.com.fiap.techchallenge.repository.CardapioRepository;
import br.com.fiap.techchallenge.repository.ItemCardapioRepository;
import br.com.fiap.techchallenge.repository.RestauranteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardapioService {
    private final CardapioRepository cardapioRepository;
    private final ItemCardapioRepository item;
    private final RestauranteRepository restauranteRepository;

    public CardapioService(CardapioRepository cardapioRepository, ItemCardapioRepository item, RestauranteRepository restauranteRepository) {
        this.cardapioRepository = cardapioRepository;
        this.item = item;
        this.restauranteRepository = restauranteRepository;
    }
    public CardapioResponse buscarCardapioDoRestaurante(Long idRestaurante){
        Cardapio cardapio = cardapioRepository.findByRestauranteId(idRestaurante).
                orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum cardapio encontrado para esse restaurante"));
        List<ItemCardapioResponse> itensResponse = cardapio.getItens().stream()
                .map(item -> new ItemCardapioResponse(item.getId(),
                        item.getNome(),
                        item.getDescricao(),
                        item.getPreco(),
                        item.getViagemSN(),
                        item.getCaminhoFoto()
                )).toList();
        CardapioResponse response = new CardapioResponse(cardapio.getId(),
                cardapio.getNome(),
                cardapio.getRestaurante().getNome(),
                itensResponse
        );
        return response;
    }
    public List<CardapioResponse> listarCardapios(){
        List<Cardapio> listaCardapios = cardapioRepository.findAll();
        var cardapios = listaCardapioResponseDTO(listaCardapios);
        return cardapios;
    }
    public ItemCardapioResponse buscarItemCardapio(Long id){
        ItemCardapio itemCardapio = item.findById(id).
                orElseThrow(() -> new RecursoNaoEncontradoException("Nenhum item encontrado"));
        return itemCardapioResponseDTO(itemCardapio);
    }
    public CardapioResponse criarCardapio(CreateCardapioRequest request){
        Restaurante restaurante = restauranteRepository.findById(request.idRestaurante()).
                orElseThrow(() -> new RecursoNaoEncontradoException("O Restaurante não foi encontrado"));

        if (cardapioRepository.findByRestauranteId(request.idRestaurante()).isPresent()){
            throw new DadoDuplicadoException("O restaurante já tem um cardápio");
        }
        Cardapio cardapio = new Cardapio(request.nomeCardapio(),restaurante);
        cardapioRepository.save(cardapio);
        return cardapioResponseDTO(cardapio);
    }
    public ItemCardapioResponse criarItemCardapio(CreateItemCardapioRequest request){
        Cardapio cardapio = cardapioRepository.findById(request.cardapio()).
                orElseThrow(() -> new RecursoNaoEncontradoException("Cardapio não encontrado"));
        ItemCardapio itemCardapio = new ItemCardapio(request,cardapio);
        item.save(itemCardapio);
        return itemCardapioResponseDTO(itemCardapio);
    }
    public CardapioResponse updateCardapio(UpdateCardapioRequest request){
        Cardapio cardapio = cardapioRepository.findById(request.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cardapio não encontrado"));
        cardapio.setNome(request.nomeCardapio());
        cardapioRepository.save(cardapio);
        return cardapioResponseDTO(cardapio);
    }
    public ItemCardapioResponse updateItemCardapio(UpdateItemCardapioRequest request){
        ItemCardapio itemCardapio = item.findById(request.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item do cardapio não encontrado"));
        itemCardapio.setNome(request.nomeItem());
        itemCardapio.setDescricao(request.descricao());
        itemCardapio.setPreco(request.preco());
        itemCardapio.setViagemSN(request.viagemSN());
        itemCardapio.setCaminhoFoto("https://nuvem/minhanuvem/1234567/"+request.caminhoFoto());

        item.save(itemCardapio);
        return itemCardapioResponseDTO(itemCardapio);
    }
    public void deletarCardapio(Long id){
        var cardapio = cardapioRepository.findById(id);
        if (cardapio.isPresent()){
            cardapioRepository.deleteById(id);
        }else {
            throw new RecursoNaoEncontradoException("Cardapio não encontrado no cardápio");
        }
    }
    public void deletarItemCardapio(Long id){
        var itemCardapio = item.findById(id);
        if (itemCardapio.isPresent()){
            item.deleteById(id);
        }else {
            throw new RecursoNaoEncontradoException("Item não encontrado no cardápio");
        }
    }
    private ItemCardapioResponse itemCardapioResponseDTO(ItemCardapio itemCardapio){
        return new ItemCardapioResponse(
                itemCardapio.getId(),
                itemCardapio.getNome(),
                itemCardapio.getDescricao(),
                itemCardapio.getPreco(),
                itemCardapio.getViagemSN(),
                itemCardapio.getCaminhoFoto()
        );
    }
    private List<CardapioResponse> listaCardapioResponseDTO(List<Cardapio> cardapioLista){
        return cardapioLista.stream().map(cardapio -> {
            List<ItemCardapioResponse> itensResponse = cardapio.getItens() == null ? List.of() :
                    cardapio.getItens().stream()
                            .map(item -> new ItemCardapioResponse(
                                    item.getId(),
                                    item.getNome(),
                                    item.getDescricao(),
                                    item.getPreco(),
                                    item.getViagemSN(),
                                    item.getCaminhoFoto()
                            ))
                            .toList();

            return new CardapioResponse(
                    cardapio.getId(),
                    cardapio.getNome(),
                    cardapio.getRestaurante().getNome(),
                    itensResponse
            );
        }).toList();
    }
    private CardapioResponse cardapioResponseDTO(Cardapio cardapio){
        List<ItemCardapioResponse> itensResponse =
                cardapio.getItens() == null ? List.of() :
                        cardapio.getItens().stream()
                                .map(this::itemCardapioResponseDTO)
                                .toList();
        return new CardapioResponse(
                cardapio.getId(),
                cardapio.getNome(),
                cardapio.getRestaurante().getNome(),
                itensResponse
        );
    }
    }