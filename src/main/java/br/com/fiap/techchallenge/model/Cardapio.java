package br.com.fiap.techchallenge.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;


@Entity
@Table(name = "cardapio")
@Getter
public class Cardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", referencedColumnName = "id")
    private Restaurante restaurante;
    @OneToMany(mappedBy = "cardapio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCardapio> itens;
    public Cardapio() {
    }
    public Cardapio(String nome, Restaurante restaurante) {
        this.nome = nome;
        this.restaurante = restaurante;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
}
