package br.com.fiap.techchallenge.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "tipo_cozinha")
@Getter
    public class TipoCozinha {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String tipo;
        public enum Values{
        ILHA(1),
        ZONAS(2),
        MONTAGEM(3),
        PARALELA(4);
        long id;
        Values(long id){
            this.id=id;
        }
    }
    }