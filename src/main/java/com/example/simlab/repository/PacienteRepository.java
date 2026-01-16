package com.example.simlab.repository;

import com.example.simlab.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
Page<Paciente> findByNome (String nome, Pageable pageable);
Page<Paciente>findByDataDeNascimento (LocalDate dataDeNascimento,Pageable pageable);
Page<Paciente> findByCartaoCidadao (String cartaoCidadao, Pageable pageable);
Page<Paciente>findByNomeAndCartaoCidadao ( String nome, String cartaoCidadao, Pageable pageable);
boolean existsByCartaoCidadaoIgnoreCase ( String cartaoCidadao);
}
