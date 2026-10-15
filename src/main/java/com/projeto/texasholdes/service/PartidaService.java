package com.projeto.texasholdes.service;

import com.projeto.texasholdes.dto.response.OperacaoPartidaResponse;
import com.projeto.texasholdes.dto.response.PartidaAbertaResponse;
import com.projeto.texasholdes.entity.Carta;
import com.projeto.texasholdes.entity.Jogador;
import com.projeto.texasholdes.entity.Partida;
import com.projeto.texasholdes.entity.StatusPartida;
import com.projeto.texasholdes.exception.BusinessException;
import com.projeto.texasholdes.exception.ResourceNotFoundException;
import com.projeto.texasholdes.repository.JogadorRepository;
import com.projeto.texasholdes.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final JogadorRepository jogadorRepository;
    private final BaralhoService baralhoService;

    @Transactional(readOnly = true)
    public List<PartidaAbertaResponse> listarPartidasAbertas() {
        return partidaRepository.findAllByStatus(StatusPartida.AGUARDANDO_JOGADORES).stream()
                .map(partida -> PartidaAbertaResponse.builder()
                        .idPartida(partida.getId())
                        .quantidadeJogadores(partida.getQuantidadeJogadores())
                        .maxJogadores(partida.getMaxJogadores())
                        .status(partida.getStatus().name())
                        .build())
                .toList();
    }

    @Transactional
    public OperacaoPartidaResponse entrarNaPartida(Long idPartida, Long idJogador) {
        Partida partida = buscarPartidaOuFalhar(idPartida);
        Jogador jogador = buscarJogadorOuFalhar(idJogador);

        if (partida.getStatus() != StatusPartida.AGUARDANDO_JOGADORES) {
            throw new BusinessException("A partida não está aberta para novos participantes.");
        }

        if (partida.getQuantidadeJogadores() >= partida.getMaxJogadores()) {
            throw new BusinessException("A mesa está cheia.");
        }

        boolean jaPresente = partida.getJogadores().stream()
                .anyMatch(j -> j.getId().equals(jogador.getId()));

        if (jaPresente) {
            throw new BusinessException("O jogador já está nesta partida.");
        }

        partida.getJogadores().add(jogador);
        partidaRepository.save(partida);

        return OperacaoPartidaResponse.builder()
                .sucesso(true)
                .mensagem("Jogador entrou na partida com sucesso.")
                .build();
    }

    @Transactional
    public OperacaoPartidaResponse iniciarPartida(Long idPartida, Long idJogadorResponsavel) {
        Partida partida = buscarPartidaOuFalhar(idPartida);

        if (idJogadorResponsavel != null && !partida.getCriador().getId().equals(idJogadorResponsavel)) {
            throw new BusinessException("Apenas o jogador responsável pela criação da mesa pode iniciá-la.");
        }

        if (partida.getStatus() != StatusPartida.AGUARDANDO_JOGADORES) {
            throw new BusinessException("A partida já foi iniciada ou finalizada.");
        }

        if (partida.getQuantidadeJogadores() < 2) {
            throw new BusinessException("Não há jogadores suficientes para iniciar o jogo. Mínimo de 2 participantes.");
        }

        distribuirCartasAosJogadores(partida);

        partida.setStatus(StatusPartida.EM_ANDAMENTO);
        partidaRepository.save(partida);

        return OperacaoPartidaResponse.builder()
                .sucesso(true)
                .mensagem("Partida iniciada e cartas distribuídas com sucesso.")
                .build();
    }

    private void distribuirCartasAosJogadores(Partida partida) {
        List<Carta> baralho = baralhoService.criarBaralhoEmbaralhado();
        Map<Long, String> cartasJogadores = new HashMap<>();

        int indiceCarta = 0;
        for (Jogador participante : partida.getJogadores()) {
            Carta carta1 = baralho.get(indiceCarta++);
            Carta carta2 = baralho.get(indiceCarta++);
            cartasJogadores.put(participante.getId(), carta1 + ", " + carta2);
        }

        partida.setCartasJogadores(cartasJogadores);
    }

    private Partida buscarPartidaOuFalhar(Long idPartida) {
        return partidaRepository.findById(idPartida)
                .orElseThrow(() -> new ResourceNotFoundException("Partida não encontrada com ID: " + idPartida));
    }

    private Jogador buscarJogadorOuFalhar(Long idJogador) {
        return jogadorRepository.findById(idJogador)
                .orElseThrow(() -> new ResourceNotFoundException("Jogador não encontrado com ID: " + idJogador));
    }
}
