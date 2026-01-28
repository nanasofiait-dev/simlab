package com.example.simlab.service;

import com.example.simlab.dto.PacienteDTO;
import com.example.simlab.dto.PacienteDetalheDTO;
import com.example.simlab.dto.PacienteUpdateDTO;
import com.example.simlab.exception.DuplicadoException;
import com.example.simlab.exception.RecursoNaoEncontradoException;
import com.example.simlab.model.Paciente;
import com.example.simlab.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do PacienteService")
public class PacienteServiceTest {

    @Mock
    private PacienteRepository repository;

    @InjectMocks
    private PacienteService service;

    private PacienteDTO dto;
    private Paciente paciente;
    private PacienteUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        dto = new PacienteDTO(
                "Maria Silva",
                LocalDate.of(1990, 1, 15),
                "12345678",
                "912345678",
                "maria@email.com"
        );

        paciente = new Paciente(
                "Maria Silva",
                LocalDate.of(1990, 1, 15),
                "12345678",
                "912345678",
                "maria@email.com"
        );
        paciente.setId(1L);

        updateDTO = new PacienteUpdateDTO(
                "Maria Silva Santos",
                LocalDate.of(1990, 1, 15),
                "12345678",
                "919999999",
                "maria.santos@email.com"
        );
    }

    //  TESTES DO MÉTODO CRIAR

    @Test
    @DisplayName("Deve criar paciente com sucesso")
    void testCriarPacienteComSucesso() {

        when(repository.existsByCartaoCidadaoIgnoreCase(dto.getCartaoCidadao())).thenReturn(false);
        when(repository.save(any(Paciente.class))).thenReturn(paciente);

        PacienteDetalheDTO resultado = service.criar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Maria Silva", resultado.getNome());
        assertEquals("12345678", resultado.getCartaoCidadao());

        verify(repository, times(1)).existsByCartaoCidadaoIgnoreCase(dto.getCartaoCidadao());
        verify(repository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar paciente com CC duplicado")
    void testCriarPacienteComCCDuplicado() {

        when(repository.existsByCartaoCidadaoIgnoreCase(dto.getCartaoCidadao())).thenReturn(true);


        assertThrows(DuplicadoException.class, () -> {
            service.criar(dto);
        });

        verify(repository, never()).save(any(Paciente.class));
    }

    //TESTES DO MÉTODO LISTAR

    @Test
    @DisplayName("Deve listar todos os pacientes sem filtros")
    void testListarTodosPacientes() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Paciente> paginaMock = new PageImpl<>(List.of(paciente));

        when(repository.findAll(pageable)).thenReturn(paginaMock);

        Page<PacienteDTO> resultado = service.listar(null, null, null, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve listar pacientes filtrados por nome")
    void testListarPacientesPorNome() {

        String nome = "Maria Silva";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Paciente> paginaMock = new PageImpl<>(List.of(paciente));

        when(repository.findByNomeIgnoreCase(nome, pageable)).thenReturn(paginaMock);

        Page<PacienteDTO> resultado = service.listar(nome, null, null, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(repository).findByNomeIgnoreCase(nome, pageable);
    }

    @Test
    @DisplayName("Deve listar pacientes filtrados por Cartão de Cidadão")
    void testListarPacientesPorCC() {

        String cc = "12345678";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Paciente> paginaMock = new PageImpl<>(List.of(paciente));

        when(repository.findByCartaoCidadaoIgnoreCase(cc, pageable)).thenReturn(paginaMock);

        Page<PacienteDTO> resultado = service.listar(null, null, cc, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(repository).findByCartaoCidadaoIgnoreCase(cc, pageable);
    }

    @Test
    @DisplayName("Deve listar pacientes filtrados por data de nascimento")
    void testListarPacientesPorDataNascimento() {

        LocalDate dataNascimento = LocalDate.of(1990, 1, 15);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Paciente> paginaMock = new PageImpl<>(List.of(paciente));

        when(repository.findByDataDeNascimento(dataNascimento, pageable)).thenReturn(paginaMock);

        Page<PacienteDTO> resultado = service.listar(null, dataNascimento, null, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(repository).findByDataDeNascimento(dataNascimento, pageable);
    }

    @Test
    @DisplayName("Deve listar pacientes filtrados por nome e CC")
    void testListarPacientesPorNomeECC() {
        String nome = "Maria Silva";
        String cc = "12345678";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Paciente> paginaMock = new PageImpl<>(List.of(paciente));

        when(repository.findByNomeIgnoreCaseAndCartaoCidadaoIgnoreCase(nome,cc, pageable))
                .thenReturn(paginaMock);

        Page<PacienteDTO> resultado = service.listar(nome,null, cc, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(repository).findByNomeIgnoreCaseAndCartaoCidadaoIgnoreCase(nome, cc, pageable);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não encontrar pacientes")
    void testListarPacientesVazio() {

        String nome = "Nome Inexistente";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Paciente> paginaVazia = new PageImpl<>(List.of());

        when(repository.findByNomeIgnoreCase(nome, pageable)).thenReturn(paginaVazia);

        Page<PacienteDTO> resultado = service.listar(nome, null, null, pageable);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());
        assertTrue(resultado.isEmpty());
    }

    //TESTES DO MÉTODO BUSCAR POR ID

    @Test
    @DisplayName("Deve buscar paciente por ID com sucesso")
    void testBuscarPorIdComSucesso() {

        when(repository.findById(1L)).thenReturn(Optional.of(paciente));

        Optional<PacienteDetalheDTO> resultado = service.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Maria Silva", resultado.get().getNome());
        assertEquals("12345678", resultado.get().getCartaoCidadao());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando ID não existe")
    void testBuscarPorIdNaoExiste() {

        Long idInexistente = 999L;
        when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        Optional<PacienteDetalheDTO> resultado = service.buscarPorId(idInexistente);

        assertTrue(resultado.isEmpty());
        verify(repository).findById(idInexistente);
    }

    //TESTES DO MÉTODO ATUALIZAR

    @Test
    @DisplayName("Deve atualizar paciente com sucesso")
    void testAtualizarPacienteComSucesso() {

        when(repository.findById(1L)).thenReturn(Optional.of(paciente));
        when(repository.save(any(Paciente.class))).thenReturn(paciente);

        PacienteDetalheDTO resultado = service.atualizar(1L, updateDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository).findById(1L);
        verify(repository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar paciente inexistente")
    void testAtualizarPacienteInexistente() {

        Long idInexistente = 999L;
        when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            service.atualizar(idInexistente, updateDTO);
        });

        verify(repository, never()).save(any(Paciente.class));
    }

    //TESTES DO MÉTODO APAGAR

    @Test
    @DisplayName("Deve apagar paciente com sucesso")
    void testApagarPacienteComSucesso() {

        when(repository.existsById(1L)).thenReturn(true);

        boolean resultado = service.apagar(1L);

        assertTrue(resultado);
        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve retornar false ao tentar apagar paciente inexistente")
    void testApagarPacienteInexistente() {

        Long idInexistente = 999L;
        when(repository.existsById(idInexistente)).thenReturn(false);

        boolean resultado = service.apagar(idInexistente);

        assertFalse(resultado);
        verify(repository, never()).deleteById(any());
    }
}
