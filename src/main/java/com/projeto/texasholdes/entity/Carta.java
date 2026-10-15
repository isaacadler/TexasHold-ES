package com.projeto.texasholdes.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Carta {
    private final String valor;
    private final String naipe;

    @Override
    public String toString() {
        return valor + " de " + naipe;
    }
}
