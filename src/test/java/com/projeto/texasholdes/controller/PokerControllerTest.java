package com.projeto.texasholdes.controller;

import com.projeto.texasholdes.dto.request.CadastroJogadorRequest;
import com.projeto.texasholdes.dto.response.CadastroJogadorResponse;
import com.projeto.texasholdes.dto.response.OperacaoPartidaResponse;
import com.projeto.texasholdes.dto.response.PartidaAbertaResponse;
import com.projeto.texasholdes.service.JogadorService;
import com.projeto.texasholdes.service.PartidaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokerControllerTest {

    @Mock
    private JogadorService jogadorService;

    @Mock
    private PartidaService partidaService;

    @InjectMocks
    private PokerController pokerController;

    @Test
    @DisplayName("US01: POST /poker/cadastro - deve responder HTTP 201 com ID")
    void deveCadastrarJogador() {
        CadastroJogadorRequest request = new CadastroJogadorRequest("Felipe", "felipe@teste.com", "senha123");
        when(jogadorService.cadastrar(any(CadastroJogadorRequest.class)))
                .thenReturn(new CadastroJogadorResponse(1L));

        ResponseEntity<CadastroJogadorResponse> response = pokerController.cadastrar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(jogadorService).cadastrar(request);
    }

    @Test
    @DisplayName("US09: GET /poker/partidasAbertas - deve responder HTTP 200 com partidas abertas")
    void deveListarPartidasAbertas() {
        when(partidaService.listarPartidasAbertas()).thenReturn(List.of(
                PartidaAbertaResponse.builder()
                        .idPartida(10L)
                        .quantidadeJogadores(3)
                        .maxJogadores(8)
                        .status("AGUARDANDO_JOGADORES")
                        .build()
        ));

        ResponseEntity<List<PartidaAbertaResponse>> response = pokerController.listarPartidasAbertas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(10L, response.getBody().get(0).getIdPartida());
    }

    @Test
    @DisplayName("US10: POST /poker/{idPartida}/entrar - deve responder HTTP 200 ao entrar")
    void deveEntrarNaPartida() {
        when(partidaService.entrarNaPartida(10L, 5L)).thenReturn(
                OperacaoPartidaResponse.builder().sucesso(true).mensagem("Entrou").build()
        );

        ResponseEntity<OperacaoPartidaResponse> response = pokerController.entrarNaPartida(10L, 5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSucesso());
    }

    @Test
    @DisplayName("US12 e US13: POST /poker/{idPartida}/iniciar - deve responder HTTP 200 ao iniciar")
    void deveIniciarPartida() {
        when(partidaService.iniciarPartida(10L, 1L)).thenReturn(
                OperacaoPartidaResponse.builder().sucesso(true).mensagem("Iniciada").build()
        );

        ResponseEntity<OperacaoPartidaResponse> response = pokerController.iniciarPartida(10L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSucesso());
    }
}
