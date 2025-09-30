package br.com.fiap.techchallenge.repository;

import br.com.fiap.techchallenge.dto.CreateItemCardapioRequest;
import br.com.fiap.techchallenge.model.Cardapio;
import br.com.fiap.techchallenge.model.ItemCardapio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemCardapioRepositoryTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;
    @Test
    public void deveBuscarPorId(){
        var itemCardapio = criarItemCardapioTeste();
        when(itemCardapioRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(itemCardapio));

        var buscaId = itemCardapioRepository.findById(1L);

        assertThat(buscaId.get()).isInstanceOf(ItemCardapio.class).isNotNull().isEqualTo(itemCardapio);
    }
    @Test
    public void deveCriarItemDoCardapio(){
        var itemCardapio = criarItemCardapioTeste();
        when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(itemCardapio);

        var itemCardapioCriado = itemCardapioRepository.save(itemCardapio);

        assertThat(itemCardapioCriado).isNotNull();
        verify(itemCardapioRepository,times(1)).save(itemCardapio);
    }

    @Test
    public void deveExcluirItemDoCardapio(){
        var itemCardapio = criarItemCardapioTeste();
        doNothing().when(itemCardapioRepository).deleteById(any(Long.class));

        itemCardapioRepository.deleteById(1L);

        verify(itemCardapioRepository,times(1)).deleteById(1L);

    }
    private ItemCardapio criarItemCardapioTeste(){
        Cardapio cardapio = new Cardapio();
        CreateItemCardapioRequest request = new CreateItemCardapioRequest(
                "Pizza Margherita",
        "Deliciosa pizza artesanal com molho de tomate, mussarela fresca e manjericão.",
                new BigDecimal("39.90"),
         true,
        "pizza-margherita.jpg",
         1L
        );
        return new ItemCardapio(request,cardapio);
    }

}
