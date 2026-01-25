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

/**
 * Service responsável pela lógica de negócio do Exame.
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */
@Service
public class ExameService {
    private final ExameRepository exameRepository;
    private final PacienteRepository pacienteRepository;

    public ExameService(ExameRepository exameRepository, PacienteRepository pacienteRepository) {
        this.exameRepository = exameRepository;
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Cria um exame novo no sistema.
     *
     * @param dto Dados do exame a ser criado
     * @return Detalhe do exame criado com ID gerado
     * @throws DuplicadoException se já existe exame com o mesmo nome
     * @throws RecursoNaoEncontradoException se o paciente não for encontrado
     */
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

    /**
     * Lista exames que com filtros opcionais e paginação.
     *
     * @param nome Nome do exame para filtrar(opcional)
     * @param descricao Descrição sobre o que é o exame para filtrar (opcional)
     * @param pageable Configuração de página e ordenação
     * @return Página contendo exames correspondêntes ao filtro
     */
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

    /**
     * Buscar exame pelo identificador único do exame.
     *
     * @param id Identificação única do exame
     * @return Optional contendo o exame  se encontrado, vazio caso contrário
     */
    public Optional<ExameDetalheDTO> buscarPorId(Long id) {

        return exameRepository.findById(id).map(exame -> new ExameDetalheDTO(exame.getId(),exame.getNome(),exame.getDescricao(), exame.getPreco(), exame.getPaciente().getId()));


    }

    /**
     * Atualizar dados sobre o exame.
     *
     * @param id Identificação única do exame
     * @param dto Novos dados do exame
     * @return Exame detalhe do exame atualizado
     * @throws RecursoNaoEncontradoException se o exame não for encontrado
     */
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

    /**
     * Apaga exame da base de dados.
     *
     * @param id Identificação única do exame
     * @return True se o exame foi apagado, false se não foi encontrado
     */
    public boolean apagar(Long id){
        if (exameRepository.existsById(id)){
            exameRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
