package com.projeto.texasholdes.controller;

import com.projeto.texasholdes.dto.request.CadastroJogadorRequest;
import com.projeto.texasholdes.dto.response.CadastroJogadorResponse;
import com.projeto.texasholdes.dto.response.OperacaoPartidaResponse;
import com.projeto.texasholdes.dto.response.PartidaAbertaResponse;
import com.projeto.texasholdes.service.JogadorService;
import com.projeto.texasholdes.service.PartidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/poker")
@RequiredArgsConstructor
public class PokerController {

    private final JogadorService jogadorService;
    private final PartidaService partidaService;

    @PostMapping("/cadastro")
    public ResponseEntity<CadastroJogadorResponse> cadastrar(@Valid @RequestBody CadastroJogadorRequest request) {
        CadastroJogadorResponse response = jogadorService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/partidasAbertas")
    public ResponseEntity<List<PartidaAbertaResponse>> listarPartidasAbertas() {
        List<PartidaAbertaResponse> response = partidaService.listarPartidasAbertas();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{idPartida}/entrar")
    public ResponseEntity<OperacaoPartidaResponse> entrarNaPartida(
            @PathVariable Long idPartida,
            @RequestParam Long idJogador
    ) {
        OperacaoPartidaResponse response = partidaService.entrarNaPartida(idPartida, idJogador);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{idPartida}/iniciar")
    public ResponseEntity<OperacaoPartidaResponse> iniciarPartida(
            @PathVariable Long idPartida,
            @RequestParam(required = false) Long idJogadorResponsavel
    ) {
        OperacaoPartidaResponse response = partidaService.iniciarPartida(idPartida, idJogadorResponsavel);
        return ResponseEntity.ok(response);
    }
}
