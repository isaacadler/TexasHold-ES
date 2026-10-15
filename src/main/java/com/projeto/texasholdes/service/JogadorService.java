package com.projeto.texasholdes.service;

import com.projeto.texasholdes.dto.request.CadastroJogadorRequest;
import com.projeto.texasholdes.dto.response.CadastroJogadorResponse;
import com.projeto.texasholdes.entity.Jogador;
import com.projeto.texasholdes.exception.BusinessException;
import com.projeto.texasholdes.repository.JogadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JogadorService {

    private final JogadorRepository jogadorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CadastroJogadorResponse cadastrar(CadastroJogadorRequest request) {
        if (jogadorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um jogador cadastrado com o e-mail informado.");
        }

        Jogador jogador = Jogador.builder()
                .nome(request.getNome().trim())
                .email(request.getEmail().trim().toLowerCase())
                .senha(passwordEncoder.encode(request.getSenha()))
                .build();

        Jogador salvo = jogadorRepository.save(jogador);
        return new CadastroJogadorResponse(salvo.getId());
    }
}
