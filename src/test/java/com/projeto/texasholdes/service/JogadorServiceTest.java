package com.projeto.texasholdes.service;

import com.projeto.texasholdes.dto.request.CadastroJogadorRequest;
import com.projeto.texasholdes.dto.response.CadastroJogadorResponse;
import com.projeto.texasholdes.entity.Jogador;
import com.projeto.texasholdes.exception.BusinessException;
import com.projeto.texasholdes.repository.JogadorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JogadorServiceTest {

    @Mock
    private JogadorRepository jogadorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private JogadorService jogadorService;

    @Test
    @DisplayName("US01: Deve cadastrar jogador com sucesso")
    void deveCadastrarJogadorComSucesso() {
        CadastroJogadorRequest request = new CadastroJogadorRequest("Felipe", "felipe@teste.com", "senha123");

        when(jogadorRepository.existsByEmail("felipe@teste.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("hashCriptografado");

        Jogador jogadorSalvo = Jogador.builder()
                .id(1L)
                .nome("Felipe")
                .email("felipe@teste.com")
                .senha("hashCriptografado")
                .build();

        when(jogadorRepository.save(any(Jogador.class))).thenReturn(jogadorSalvo);

        CadastroJogadorResponse response = jogadorService.cadastrar(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(jogadorRepository).save(any(Jogador.class));
    }

    @Test
    @DisplayName("US01: Deve rejeitar cadastro com email duplicado")
    void deveRejeitarCadastroComEmailDuplicado() {
        CadastroJogadorRequest request = new CadastroJogadorRequest("Felipe", "felipe@teste.com", "senha123");
        when(jogadorRepository.existsByEmail("felipe@teste.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> jogadorService.cadastrar(request));
        verify(jogadorRepository, never()).save(any(Jogador.class));
    }
}
