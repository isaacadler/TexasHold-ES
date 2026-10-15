package com.projeto.texasholdes.repository;

import com.projeto.texasholdes.entity.Partida;
import com.projeto.texasholdes.entity.StatusPartida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

    @Query("SELECT p FROM Partida p LEFT JOIN FETCH p.jogadores WHERE p.status = :status")
    List<Partida> findAllByStatus(StatusPartida status);
}
