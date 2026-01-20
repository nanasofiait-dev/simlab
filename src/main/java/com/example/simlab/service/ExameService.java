package com.example.simlab.service;

import com.example.simlab.dto.ExameDTO;
import com.example.simlab.dto.ExameDetalheDTO;
import com.example.simlab.dto.ExameUpdateDTO;
import com.example.simlab.exception.DuplicadoException;
import com.example.simlab.exception.RecursoNaoEncontradoException;
import com.example.simlab.model.Exame;
import com.example.simlab.model.Paciente;
import com.example.simlab.repository.ExameRepository;
import com.example.simlab.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class ExameService {
    private final ExameRepository exameRepository;
    private final PacienteRepository pacienteRepository;

    public ExameService(ExameRepository exameRepository, PacienteRepository pacienteRepository) {
        this.exameRepository = exameRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public ExameDetalheDTO criar(ExameDTO dto) {

        if (exameRepository.existsByNome(dto.getNome())) {
            throw new DuplicadoException("Existe exame com esse nome");
        }

        Paciente paciente= pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado com ID: " + dto.getPacienteId()));

        Exame exame = new Exame();
        exame.setNome(dto.getNome());
        exame.setDescricao(dto.getDescricao());
        exame.setPreco(dto.getPreco());
        exame.setPaciente(paciente);

        Exame salvar = exameRepository.save(exame);

        return new ExameDetalheDTO(salvar.getId(), salvar.getNome(), salvar.getDescricao(), salvar.getPreco(), salvar.getPaciente().getId());


    }

    public Page<ExameDTO> listar(String nome, String descricao, Pageable pageable) {

        Page<Exame> pagina;

        if (nome != null && descricao != null) {
            pagina = exameRepository.findByNomeAndDescricaoIgnoreCase(nome, descricao, pageable);
        } else if (nome != null && !nome.isBlank()) {
            pagina =exameRepository.findByNomeIgnoreCase(nome, pageable);

        } else if (descricao != null && !descricao.isBlank()) {
            pagina = exameRepository.findByDescricaoContainingIgnoreCase(descricao, pageable);
        } else {
            pagina =exameRepository.findAll(pageable);
        }

        return pagina.map(exame -> new ExameDTO(exame.getNome(), exame.getDescricao(), exame.getPreco(), exame.getPaciente().getId()));

    }

    public Optional<ExameDetalheDTO> buscarPorId(Long id) {

        return exameRepository.findById(id).map(exame -> new ExameDetalheDTO(exame.getId(),exame.getNome(),exame.getDescricao(), exame.getPreco(), exame.getPaciente().getId()));


    }

    public ExameDetalheDTO atualizar (Long id, ExameUpdateDTO dto){
        Optional<Exame> optional= exameRepository.findById(id);

        if (optional.isEmpty()){
            throw new RecursoNaoEncontradoException("Exame não encontrado");

        }

        Exame exame=optional.get();
        exame.setNome(dto.getNome());
        exame.setDescricao(dto.getDescricao());
        exame.setPreco(dto.getPreco());

        Exame atualizada= exameRepository.save(exame);

        return new ExameDetalheDTO(atualizada.getId(),atualizada.getNome(), atualizada.getDescricao(), atualizada.getPreco(),atualizada.getPaciente().getId());
    }


    public boolean apagar(Long id){
        if (exameRepository.existsById(id)){
            exameRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
