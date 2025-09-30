package br.com.fiap.techchallenge.repository;

import br.com.fiap.techchallenge.model.Cardapio;
import br.com.fiap.techchallenge.model.Restaurante;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardapioRepositoryTest {
    @Mock
    private CardapioRepository cardapioRepository;
    @Test
    public void deveBuscarPorId(){
        var cardapio = criarCardapioTeste();
        when(cardapioRepository.findById(any(Long.class))).thenReturn(Optional.of(cardapio));

        var buscaId = cardapioRepository.findById(1L);

        assertThat(buscaId.get()).isInstanceOf(Cardapio.class).isNotNull().isEqualTo(cardapio);
    }
    @Test
    public void deveBuscarTodosCardapios(){
        var cardapio = criarCardapioTeste();
        List<Cardapio> cardapios = new ArrayList<>();
        cardapios.add(cardapio);
        when(cardapioRepository.findAll()).thenReturn(cardapios);

        var listaCardapios = cardapioRepository.findAll();

        Assertions.assertNotNull(listaCardapios);
        Assertions.assertEquals(cardapios,listaCardapios);
        Assertions.assertEquals(cardapios.size(),listaCardapios.size());
    }
    @Test
    public void deveBuscarCardapioPorIdDoRestaurante(){
        var cardapio = criarCardapioTeste();
        when(cardapioRepository.findByRestauranteId(any(Long.class)))
                .thenReturn(Optional.of(cardapio));

        var buscaIdRestaurante = cardapioRepository.findByRestauranteId(1L);

        assertThat(buscaIdRestaurante.get())
                .isInstanceOf(Cardapio.class)
                .isNotNull()
                .isEqualTo(cardapio);

    }
    @Test
    public void deveCriarCardapio(){
        var cardapio = criarCardapioTeste();
        when(cardapioRepository.save(any(Cardapio.class))).thenReturn(cardapio);

        var cardapioCriado = cardapioRepository.save(cardapio);

        assertThat(cardapioCriado).isNotNull();
        verify(cardapioRepository,times(1)).save(cardapio);
    }
    @Test
    public void deveDeletarCardapio(){
        var cardapio = criarCardapioTeste();
        doNothing().when(cardapioRepository).deleteById(any(Long.class));

        cardapioRepository.deleteById(1L);

        verify(cardapioRepository,times(1)).deleteById(1L);

    }

    private Cardapio criarCardapioTeste() {
        Restaurante restaurante = new Restaurante();
        return new Cardapio("Cardápio de Verão", restaurante);
    }
}
