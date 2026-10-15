package com.projeto.texasholdes.service;

import com.projeto.texasholdes.entity.Carta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BaralhoServiceTest {

    private final BaralhoService baralhoService = new BaralhoService();

    @Test
    @DisplayName("Deve gerar baralho completo com 52 cartas únicas")
    void deveGerarBaralhoCompleto() {
        List<Carta> baralho = baralhoService.criarBaralhoCompleto();

        assertEquals(52, baralho.size());

        Set<Carta> cartasUnicas = new HashSet<>(baralho);
        assertEquals(52, cartasUnicas.size(), "O baralho deve conter 52 cartas distintas");
    }

    @Test
    @DisplayName("Deve gerar baralho embaralhado com 52 cartas únicas")
    void deveGerarBaralhoEmbaralhado() {
        List<Carta> baralho = baralhoService.criarBaralhoEmbaralhado();

        assertEquals(52, baralho.size());
        Set<Carta> cartasUnicas = new HashSet<>(baralho);
        assertEquals(52, cartasUnicas.size());
    }
}
