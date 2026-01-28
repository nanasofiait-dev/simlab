package com.example.simlab.service;

import com.example.simlab.dto.PacienteDTO;
import com.example.simlab.dto.PacienteDetalheDTO;
import com.example.simlab.dto.PacienteUpdateDTO;
import com.example.simlab.exception.DuplicadoException;
import com.example.simlab.exception.RecursoNaoEncontradoException;
import com.example.simlab.model.Paciente;
import com.example.simlab.repository.PacienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service responsável pela lógica de negócio relacionada a pacientes.
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */
@Service
public class PacienteService {
    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    /**
     * Cria um novo paciente no sistema
     *
     * @param dto Dados do paciente a ser criado
     * @return Detalhes do paciente criado com ID gerado
     * @throws DuplicadoException se o Cartão Cidadão já existir no sistema
     */

    public PacienteDetalheDTO criar(PacienteDTO dto) {
        // se existir cc igual , lança exception
        if (repository.existsByCartaoCidadaoIgnoreCase(dto.getCartaoCidadao())) {
            throw new DuplicadoException("Não é possível cadastrar paciente, pois já existe paciente com este Cartão Cidadão");
        }

        // se não existir,  crio uma variável que irá  copiar os dados de dto para entity
        Paciente paciente = new Paciente();
        paciente.setNome(dto.getNome());
        paciente.setDataDeNascimento(dto.getDataDeNascimento());
        paciente.setCartaoCidadao(dto.getCartaoCidadao());
        paciente.setTelefone(dto.getTelefone());
        paciente.setEmail(dto.getEmail());


        // tem que salvar na bd para criar id
        Paciente salvar = repository.save(paciente);

        // retorna uma paciente detalhe e transforma entity em dto
        return new PacienteDetalheDTO(salvar.getId(), salvar.getNome(), salvar.getDataDeNascimento(), salvar.getCartaoCidadao(), salvar.getTelefone(), salvar.getEmail());

    }

    /**
     * Lista pacientes com filtros e paginação.
     *
     * @param nome             Nome do paciente para filtrar (opcional)
     * @param dataDeNascimento Data de paciente para filtrar(opcional)
     * @param cartaoCidadao    Cartão de Cidadão para filtrar(opcional)
     * @param pageable         Configuração de paginação e ordenação
     * @return Página contendo pacientes que correspondem ao filtro
     */
    public Page<PacienteDTO> listar(String nome, LocalDate dataDeNascimento, String cartaoCidadao, Pageable pageable) {

        Page<Paciente> pagina;

        if (nome != null && cartaoCidadao != null) {
            pagina = repository.findByNomeIgnoreCaseAndCartaoCidadaoIgnoreCase(nome, cartaoCidadao, pageable);
        } else if (nome != null && !nome.isBlank()) {
            pagina = repository.findByNomeIgnoreCase(nome, pageable);
        } else if (cartaoCidadao != null && !cartaoCidadao.isBlank()) {
            pagina = repository.findByCartaoCidadaoIgnoreCase(cartaoCidadao, pageable);
        } else if (dataDeNascimento != null) {
            pagina = repository.findByDataDeNascimento(dataDeNascimento, pageable);
        } else {
            pagina = repository.findAll(pageable);
        }

        return pagina.map(paciente -> new PacienteDTO(
                paciente.getNome(),
                paciente.getDataDeNascimento(),
                paciente.getCartaoCidadao(),
                paciente.getTelefone(),
                paciente.getEmail()
        ));
    }

    /**
     * Busca um paciente pelo seu identificador único.
     *
     * @param id Identificador único do paciente
     * @return Optional contendo o paciente se encontrado, e vazio caso contrário
     */
    public Optional<PacienteDetalheDTO> buscarPorId(Long id) {

        return repository.findById(id).map(paciente -> new PacienteDetalheDTO(paciente.getId(), paciente.getNome(), paciente.getDataDeNascimento(), paciente.getCartaoCidadao(), paciente.getTelefone(), paciente.getEmail()));
    }

/**
 * Atualiza os dados de um paciente existente.
 *
 * @param id Identificador único do paciente
 * @param dto Novos dados do paciente
 * @return Detalhes do paciente atualizado
 * @throws RecursoNaoEncontradoException se o paciente não for encontrado
 */
        public PacienteDetalheDTO atualizar (Long id, PacienteUpdateDTO dto ){

            //pesquiso se existe ID
            Optional<Paciente> optional = repository.findById(id);

            if (optional.isEmpty()) {
                throw new RecursoNaoEncontradoException("Paciente não foi encontrado");
            }

            // se existe preciso buscar esse paciente em optional
            Paciente paciente = optional.get();

            // vai substituir os valores antigos pelos novos
            paciente.setNome(dto.getNome());
            paciente.setDataDeNascimento(dto.getDataDeNascimento());
            paciente.setCartaoCidadao(dto.getCartaoCidadao());
            paciente.setTelefone(dto.getTelefone());
            paciente.setEmail(dto.getEmail());

            //aqui o spring vê id e atualiza na bd
            Paciente atualizada = repository.save(paciente);

            return new PacienteDetalheDTO(
                    atualizada.getId(),
                    atualizada.getNome(),
                    atualizada.getDataDeNascimento(),
                    atualizada.getCartaoCidadao(),
                    atualizada.getTelefone(),
                    atualizada.getEmail());
        }

        /**
         * Apaga um paciente da base de dados.
         *
         * @param id Identificador único do paciente
         * @return Retorna true quando apagado e false quando não é encontrado
         */

        public boolean apagar (Long id){
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return true;
            }
            return false;
        }

    }
