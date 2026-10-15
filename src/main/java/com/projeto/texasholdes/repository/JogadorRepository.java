package com.projeto.texasholdes.repository;

import com.projeto.texasholdes.entity.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long> {
    boolean existsByEmail(String email);
    Optional<Jogador> findByEmail(String email);
}
