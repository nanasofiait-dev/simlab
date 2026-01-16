package com.example.simlab.repository;

import com.example.simlab.model.Exame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExameRepository extends JpaRepository<Exame,Long> {

Page<Exame> findByNomeIgnoreCase (String nome, Pageable pageable);
boolean existsByNome(String nome);
Page<Exame> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);
Page<Exame> findByNomeAndDescricaoIgnoreCase (String nome, String descricao, Pageable pageable);


}
