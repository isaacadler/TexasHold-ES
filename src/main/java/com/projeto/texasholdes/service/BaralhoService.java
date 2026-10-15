package com.projeto.texasholdes.service;

import com.projeto.texasholdes.entity.Carta;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BaralhoService {

    private static final String[] NAIPES = {"Copas", "Espadas", "Ouros", "Paus"};
    private static final String[] VALORES = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Valete", "Dama", "Rei", "Ás"};

    public List<Carta> criarBaralhoCompleto() {
        List<Carta> baralho = new ArrayList<>();
        for (String naipe : NAIPES) {
            for (String valor : VALORES) {
                baralho.add(new Carta(valor, naipe));
            }
        }
        return baralho;
    }

    public List<Carta> criarBaralhoEmbaralhado() {
        List<Carta> baralho = criarBaralhoCompleto();
        Collections.shuffle(baralho, new SecureRandom());
        return baralho;
    }
}
