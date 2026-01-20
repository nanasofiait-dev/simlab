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

@Service
public class PacienteService {
    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }


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

    public Page<PacienteDTO> listar(String nome, LocalDate dataDeNascimento, String cartaoCidadao, Pageable pageable) {

        Page<Paciente> pagina;
        if (nome != null && cartaoCidadao != null) {
            pagina = repository.findByNomeAndCartaoCidadao(nome, cartaoCidadao, pageable);
        } else if (nome != null && ! nome.isBlank()) {
            pagina = repository.findByNome(nome, pageable);

        } else if (cartaoCidadao != null && ! cartaoCidadao.isBlank()) {
            pagina = repository.findByCartaoCidadao(cartaoCidadao, pageable);

        } else if (dataDeNascimento != null) {
            pagina = repository.findByDataDeNascimento(dataDeNascimento, pageable);

        } else {
            pagina = repository.findAll(pageable);
        }


        return pagina.map(paciente -> new PacienteDTO(paciente.getNome(), paciente.getDataDeNascimento(), paciente.getCartaoCidadao(), paciente.getTelefone(), paciente.getEmail()));
    }


    public Optional<PacienteDetalheDTO> buscarPorId(Long id) {

        return repository.findById(id).map(paciente -> new PacienteDetalheDTO(paciente.getId(), paciente.getNome(), paciente.getDataDeNascimento(), paciente.getCartaoCidadao(), paciente.getTelefone(), paciente.getEmail()));
    }

    public PacienteDetalheDTO atualizar(Long id, PacienteUpdateDTO dto) {

        //pesquiso se existe ID
        Optional<Paciente> optional = repository.findById(id);

        if (optional.isEmpty()) {
            throw new RecursoNaoEncontradoException("Paciente não foi encontrado");
        }

        // se existe preciso buscar esse paciente em optional
        Paciente paciente = optional.get();

        // vai bustituit os valores antigos pelos novos
        paciente.setNome(dto.getNome());
        paciente.setDataDeNascimento(dto.getDataDeNascimento());
        paciente.setCartaoCidadao(dto.getCartaoCidadao());
        paciente.setTelefone(dto.getTelefone());
        paciente.setEmail(dto.getEmail());

        //aqui o spring vê id e atualiza na bd
        Paciente atualizada = repository.save(paciente);

        return new PacienteDetalheDTO(atualizada.getId(), atualizada.getNome(), atualizada.getDataDeNascimento(), atualizada.getCartaoCidadao(), atualizada.getTelefone(), atualizada.getEmail());
    }


    public boolean apagar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

}
