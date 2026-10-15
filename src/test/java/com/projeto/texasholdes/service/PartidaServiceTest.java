package com.projeto.texasholdes.service;

import com.projeto.texasholdes.dto.response.OperacaoPartidaResponse;
import com.projeto.texasholdes.dto.response.PartidaAbertaResponse;
import com.projeto.texasholdes.entity.Carta;
import com.projeto.texasholdes.entity.Jogador;
import com.projeto.texasholdes.entity.Partida;
import com.projeto.texasholdes.entity.StatusPartida;
import com.projeto.texasholdes.exception.BusinessException;
import com.projeto.texasholdes.repository.JogadorRepository;
import com.projeto.texasholdes.repository.PartidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private JogadorRepository jogadorRepository;

    @Mock
    private BaralhoService baralhoService;

    @InjectMocks
    private PartidaService partidaService;

    private Jogador criador;
    private Jogador jogador2;
    private Partida partida;

    @BeforeEach
    void setUp() {
        criador = Jogador.builder().id(1L).nome("Host").email("host@teste.com").senha("123").build();
        jogador2 = Jogador.builder().id(2L).nome("Player2").email("player2@teste.com").senha("123").build();

        partida = Partida.builder()
                .id(10L)
                .criador(criador)
                .maxJogadores(4)
                .status(StatusPartida.AGUARDANDO_JOGADORES)
                .jogadores(new ArrayList<>(List.of(criador)))
                .build();
    }

    @Test
    @DisplayName("US09: Deve listar partidas abertas")
    void deveListarPartidasAbertas() {
        when(partidaRepository.findAllByStatus(StatusPartida.AGUARDANDO_JOGADORES))
                .thenReturn(List.of(partida));

        List<PartidaAbertaResponse> abertas = partidaService.listarPartidasAbertas();

        assertEquals(1, abertas.size());
        assertEquals(10L, abertas.get(0).getIdPartida());
        assertEquals(1, abertas.get(0).getQuantidadeJogadores());
        assertEquals(4, abertas.get(0).getMaxJogadores());
    }

    @Test
    @DisplayName("US10: Deve permitir jogador entrar na mesa disponível")
    void devePermitirJogadorEntrar() {
        when(partidaRepository.findById(10L)).thenReturn(Optional.of(partida));
        when(jogadorRepository.findById(2L)).thenReturn(Optional.of(jogador2));

        OperacaoPartidaResponse response = partidaService.entrarNaPartida(10L, 2L);

        assertTrue(response.isSucesso());
        assertEquals(2, partida.getQuantidadeJogadores());
        verify(partidaRepository).save(partida);
    }

    @Test
    @DisplayName("US10: Deve rejeitar jogador já presente na mesa")
    void deveRejeitarJogadorJaPresente() {
        when(partidaRepository.findById(10L)).thenReturn(Optional.of(partida));
        when(jogadorRepository.findById(1L)).thenReturn(Optional.of(criador));

        BusinessException ex = assertThrows(BusinessException.class, () -> partidaService.entrarNaPartida(10L, 1L));
        assertEquals("O jogador já está nesta partida.", ex.getMessage());
    }

    @Test
    @DisplayName("US10: Deve rejeitar entrada se a mesa estiver cheia")
    void deveRejeitarMesaCheia() {
        partida.setMaxJogadores(1);
        when(partidaRepository.findById(10L)).thenReturn(Optional.of(partida));
        when(jogadorRepository.findById(2L)).thenReturn(Optional.of(jogador2));

        BusinessException ex = assertThrows(BusinessException.class, () -> partidaService.entrarNaPartida(10L, 2L));
        assertEquals("A mesa está cheia.", ex.getMessage());
    }

    @Test
    @DisplayName("US12 e US13: Deve iniciar partida e distribuir 2 cartas por jogador")
    void deveIniciarPartidaEDistribuirCartas() {
        partida.getJogadores().add(jogador2);

        when(partidaRepository.findById(10L)).thenReturn(Optional.of(partida));
        when(baralhoService.criarBaralhoEmbaralhado()).thenReturn(List.of(
                new Carta("Ás", "Espadas"), new Carta("Rei", "Copas"),
                new Carta("10", "Ouros"), new Carta("Valete", "Paus")
        ));

        OperacaoPartidaResponse response = partidaService.iniciarPartida(10L, 1L);

        assertTrue(response.isSucesso());
        assertEquals(StatusPartida.EM_ANDAMENTO, partida.getStatus());
        assertNotNull(partida.getCartasJogadores());
        assertEquals(2, partida.getCartasJogadores().size());
        assertEquals("Ás de Espadas, Rei de Copas", partida.getCartasJogadores().get(1L));
        assertEquals("10 de Ouros, Valete de Paus", partida.getCartasJogadores().get(2L));
        verify(partidaRepository).save(partida);
    }

    @Test
    @DisplayName("US12: Deve proibir início se jogador não for o criador")
    void deveProibirInicioPorJogadorNaoAutorizado() {
        when(partidaRepository.findById(10L)).thenReturn(Optional.of(partida));

        BusinessException ex = assertThrows(BusinessException.class, () -> partidaService.iniciarPartida(10L, 99L));
        assertEquals("Apenas o jogador responsável pela criação da mesa pode iniciá-la.", ex.getMessage());
    }

    @Test
    @DisplayName("US12: Deve impedir início se houver menos de 2 jogadores")
    void deveImpedirInicioSemMinimoDeJogadores() {
        when(partidaRepository.findById(10L)).thenReturn(Optional.of(partida));

        BusinessException ex = assertThrows(BusinessException.class, () -> partidaService.iniciarPartida(10L, 1L));
        assertEquals("Não há jogadores suficientes para iniciar o jogo. Mínimo de 2 participantes.", ex.getMessage());
    }
}
