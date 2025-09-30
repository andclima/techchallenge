package br.com.fiap.techchallenge.repository;

import br.com.fiap.techchallenge.model.TipoCozinha;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TipoCozinhaRepositoryTest {

    @Mock
    private TipoCozinhaRepository tipoCozinhaRepository;

    @Test
    public void deveBuscarTipoCozinhaPorId(){
        TipoCozinha tipoCozinha = new TipoCozinha();
        when(tipoCozinhaRepository.findById(any(Long.class))).thenReturn(Optional.of(tipoCozinha));

        var tipoCozinhaEncontrado = tipoCozinhaRepository.findById(1L);

        assertThat(tipoCozinhaEncontrado.get()).isInstanceOf(TipoCozinha.class).isNotNull().isEqualTo(tipoCozinha);
    }
    @Test
    public void deveBuscarTipoCozinhaPorIdNaoEncontrado(){

        when(tipoCozinhaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Optional<TipoCozinha> buscaTipo = tipoCozinhaRepository.findById(99L);

        assertThat(buscaTipo).isEmpty();
    }
}
