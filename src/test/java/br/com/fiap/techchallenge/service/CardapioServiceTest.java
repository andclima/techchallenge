package br.com.fiap.techchallenge.service;

import br.com.fiap.techchallenge.dto.*;
import br.com.fiap.techchallenge.model.Cardapio;
import br.com.fiap.techchallenge.model.ItemCardapio;
import br.com.fiap.techchallenge.model.Restaurante;
import br.com.fiap.techchallenge.repository.CardapioRepository;
import br.com.fiap.techchallenge.repository.ItemCardapioRepository;
import br.com.fiap.techchallenge.repository.RestauranteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardapioServiceTest {
    @InjectMocks
    private CardapioService cardapioService;

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Test
    public void deveRetornarListaDeCardapios(){
        Restaurante restaurante = new Restaurante();
        Cardapio cardapios = new Cardapio("Cardapio",restaurante);
        when(cardapioRepository.findAll()).thenReturn(List.of(cardapios));

        List<CardapioResponse> response = cardapioService.listarCardapios();

        assertThat(response).isNotNull().hasSize(1);
        assertThat(response.get(0).id()).isEqualTo(cardapios.getId());
    }
    @Test
    public void deveRetornarCardapioDoRestaurante(){
        var cardapio = criarCardapioComItensMock();
        when(cardapioRepository.findByRestauranteId(any(Long.class))).thenReturn(Optional.of(cardapio));

        var buscaCardapio = cardapioService.buscarCardapioDoRestaurante(1L);

        assertThat(buscaCardapio).isNotNull();
    }
    @Test
    public void deveEditarCardapio(){
        Restaurante restaurante = new Restaurante();
        Cardapio cardapio = new Cardapio("Cardapio",restaurante);
        var cardapioEditado = editarCardapioTesteRequest();
        when(cardapioRepository.findById(any(Long.class))).thenReturn(Optional.of(cardapio));
        when(cardapioRepository.save(any(Cardapio.class))).thenReturn(any(Cardapio.class));

        var editarCardapio = cardapioService.updateCardapio(cardapioEditado);

        assertThat(editarCardapio).isNotNull();
        assertThat(editarCardapio.nomeCardapio()).isEqualTo(cardapioEditado.nomeCardapio());


    }
    @Test
    public void deveEditarItemCardapio(){
        ItemCardapio itemCardapio = new ItemCardapio();
        var itemCardapioEditado = editarItemCardapioTesteRequest();
        when(itemCardapioRepository.findById(any(Long.class))).thenReturn(Optional.of(itemCardapio));
        when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(any(ItemCardapio.class));

        var editarItemCardapio = cardapioService.updateItemCardapio(itemCardapioEditado);

        assertThat(editarItemCardapio).isNotNull();
        assertThat(editarItemCardapio.nome()).isEqualTo(itemCardapioEditado.nomeItem());
    }
    @Test
    public void deveCriarCardapio(){
        var cardapio = criarCardapioRequestTest();

        when(restauranteRepository.findById(any(Long.class))).thenReturn(Optional.of(new Restaurante()));
        when(cardapioRepository.findByRestauranteId(any(Long.class))).thenReturn(Optional.empty());
        when(cardapioRepository.save(any(Cardapio.class))).thenReturn(any(Cardapio.class));

        var criarCardapio = cardapioService.criarCardapio(cardapio);

        assertThat(criarCardapio).isNotNull();
        verify(cardapioRepository,times(1)).save(any(Cardapio.class));
    }
    @Test
    public void deveCriarItemCardapio(){
        Cardapio cardapio = new Cardapio();
        var itemCardapio = criarItemCardapio();
        when(cardapioRepository.findById(any(Long.class))).thenReturn(Optional.of(cardapio));
        when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(any(ItemCardapio.class));

        var criarItemCardapio = cardapioService.criarItemCardapio(itemCardapio);

        assertThat(criarItemCardapio).isNotNull();
        verify(itemCardapioRepository,times(1)).save(any(ItemCardapio.class));
    }
    @Test
    public void deveExcluirCardapio(){
        Cardapio cardapio = new Cardapio();
        when(cardapioRepository.findById(any(Long.class))).thenReturn(Optional.of(cardapio));
        doNothing().when(cardapioRepository).deleteById(any(Long.class));

        cardapioService.deletarCardapio(1L);

        verify(cardapioRepository,times(1)).deleteById(1L);
    }
    @Test
    public void deveExcluirItemCardapio(){
        ItemCardapio itemCardapio = new ItemCardapio();
        when(itemCardapioRepository.
                findById(any(Long.class))).thenReturn(Optional.of(itemCardapio));
        doNothing().when(itemCardapioRepository).deleteById(any(Long.class));

        cardapioService.deletarItemCardapio(1L);

        verify(itemCardapioRepository,times(1)).deleteById(1L);
    }

    private CreateCardapioRequest criarCardapioRequestTest(){
        return new CreateCardapioRequest("Cardapio",1L);
    }
    private CreateItemCardapioRequest criarItemCardapio(){
        return new CreateItemCardapioRequest(
                "Item",
                "Descrição",
                new BigDecimal("12.90"),
                true,
                "JKSHKJS.jpg",
                1L
        );
    }
    private Cardapio criarCardapioComItensMock() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome("Restaurante Mock");

        Cardapio cardapio = new Cardapio("Cardapio Teste", restaurante);
        ItemCardapio item = new ItemCardapio();
        item.setNome("Item Teste");

        try {
            Field itensField = Cardapio.class.getDeclaredField("itens");
            itensField.setAccessible(true);
            itensField.set(cardapio, List.of(item));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cardapio;
    }

    private UpdateCardapioRequest editarCardapioTesteRequest(){
        return new UpdateCardapioRequest(1L,"Cardapio das casas");
    }

    private UpdateItemCardapioRequest editarItemCardapioTesteRequest(){
        return new UpdateItemCardapioRequest(
                1L,
                "Item editado",
                "Descrição",
                new BigDecimal("12.90"),
                true,
                "JKSHKJS.jpg"
        );
    }
}
