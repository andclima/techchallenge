package br.com.fiap.techchallenge.model;

import br.com.fiap.techchallenge.dto.CreateItemCardapioRequest;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "item_cardapio")
@Getter
public class ItemCardapio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Boolean viagemSN; //
    private String caminhoFoto;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardapio_id")
    private Cardapio cardapio;

    public ItemCardapio() {
    }

    public ItemCardapio(CreateItemCardapioRequest request, Cardapio cardapio) {
        this.nome = request.nomeItem();
        this.descricao = request.descricao();
        this.preco = request.preco();
        this.viagemSN = request.viagemSN();
        this.caminhoFoto = "https://nuvem/minhanuvem/1234567/"+request.caminhoFoto();
        this.cardapio = cardapio;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public void setViagemSN(Boolean viagemSN) {
        this.viagemSN = viagemSN;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
