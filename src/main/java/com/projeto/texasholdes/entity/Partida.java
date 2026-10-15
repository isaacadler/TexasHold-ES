package com.projeto.texasholdes.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "partidas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "criador_id", nullable = false)
    private Jogador criador;

    @Builder.Default
    @Column(nullable = false)
    private Integer maxJogadores = 8;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPartida status = StatusPartida.AGUARDANDO_JOGADORES;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "partida_jogadores",
            joinColumns = @JoinColumn(name = "partida_id"),
            inverseJoinColumns = @JoinColumn(name = "jogador_id")
    )
    private List<Jogador> jogadores = new ArrayList<>();

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "partida_cartas_jogadores", joinColumns = @JoinColumn(name = "partida_id"))
    @Column(name = "cartas")
    private Map<Long, String> cartasJogadores = new HashMap<>();

    public int getQuantidadeJogadores() {
        return jogadores != null ? jogadores.size() : 0;
    }
}
