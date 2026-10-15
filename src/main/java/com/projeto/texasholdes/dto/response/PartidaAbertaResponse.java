package com.projeto.texasholdes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidaAbertaResponse {
    private Long idPartida;
    private int quantidadeJogadores;
    private int maxJogadores;
    private String status;
}
