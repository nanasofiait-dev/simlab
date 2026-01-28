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
@DisplayName("Testes do ExameService")
public class ExameServiceTest {

    @Mock
    private ExameRepository exameRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private ExameService service;

    private ExameDTO exameDTO;
    private ExameUpdateDTO exameUpdateDTO;
    private Exame exame;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente(
                "Maria Silva",
                LocalDate.of(1985, 3, 15),
                "12345678",
                "912345678",
                "maria@email.com"
        );
        paciente.setId(1L);

        exameDTO = new ExameDTO(
                "Hemograma Completo",
                "Análise completa do sangue",
                25.50,
                1L
        );

        exameUpdateDTO = new ExameUpdateDTO(
                "Hemograma Completo Atualizado",
                "Análise completa do sangue - versão atualizada",
                30.00
        );

        exame = new Exame(
                "Hemograma Completo",
                "Análise completa do sangue",
                25.50,
                paciente
        );
        exame.setId(1L);
    }

    //TESTES DO MÉTODO CRIAR

    @Test
    @DisplayName("Deve criar exame com sucesso")
    void deveCriarExameComSucesso() {

        when(exameRepository.existsByNome(exameDTO.getNome())).thenReturn(false);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(exameRepository.save(any(Exame.class))).thenReturn(exame);

        ExameDetalheDTO resultado = service.criar(exameDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Hemograma Completo", resultado.getNome());
        assertEquals("Análise completa do sangue", resultado.getDescricao());
        assertEquals(1L, resultado.getPacienteId());

        verify(exameRepository, times(1)).existsByNome("Hemograma Completo");
        verify(pacienteRepository, times(1)).findById(1L);
        verify(exameRepository, times(1)).save(any(Exame.class));
    }

    @Test
    @DisplayName("Deve lançar DuplicadoException quando nome já existe")
    void deveLancarExcecaoQuandoNomeDuplicado() {

        when(exameRepository.existsByNome(exameDTO.getNome())).thenReturn(true);


        DuplicadoException exception = assertThrows(
                DuplicadoException.class,
                () -> service.criar(exameDTO)
        );

        assertEquals("Existe exame com esse nome", exception.getMessage());
        verify(exameRepository, never()).save(any(Exame.class));
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException quando paciente não existe")
    void deveLancarExcecaoQuandoPacienteNaoExiste() {

        when(exameRepository.existsByNome(exameDTO.getNome())).thenReturn(false);
        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        ExameDTO dtoComPacienteInexistente = new ExameDTO(
                "Hemograma Completo",
                "Teste",
                25.50,
                999L
        );


        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.criar(dtoComPacienteInexistente)
        );

        assertTrue(exception.getMessage().contains("Paciente não encontrado"));
        verify(exameRepository, never()).save(any(Exame.class));
    }

    //TESTES DO MÉTODO LISTAR

    @Test
    @DisplayName("Deve listar todos os exames sem filtros")
    void deveListarTodosExames() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Exame> paginaMock = new PageImpl<>(List.of(exame));

        when(exameRepository.findAll(pageable)).thenReturn(paginaMock);

        Page<ExameDTO> resultado = service.listar(null, null, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(exameRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve listar exames filtrados por nome")
    void deveListarExamesPorNome() {

        String nome = "Hemograma";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exame> paginaMock = new PageImpl<>(List.of(exame));

        when(exameRepository.findByNomeIgnoreCase(nome, pageable)).thenReturn(paginaMock);

        Page<ExameDTO> resultado = service.listar(nome, null, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Hemograma Completo", resultado.getContent().get(0).getNome());
        verify(exameRepository).findByNomeIgnoreCase(nome, pageable);
    }

    @Test
    @DisplayName("Deve listar exames filtrados por descrição")
    void deveListarExamesPorDescricao() {

        String descricao = "sangue";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exame> paginaMock = new PageImpl<>(List.of(exame));

        when(exameRepository.findByDescricaoContainingIgnoreCase(descricao, pageable))
                .thenReturn(paginaMock);


        Page<ExameDTO> resultado = service.listar(null, descricao, pageable);


        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(exameRepository).findByDescricaoContainingIgnoreCase(descricao, pageable);
    }

    @Test
    @DisplayName("Deve listar exames filtrados por nome e descrição")
    void deveListarExamesPorNomeEDescricao() {

        String nome = "Hemograma";
        String descricao = "sangue";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exame> paginaMock = new PageImpl<>(List.of(exame));

        when(exameRepository.findByNomeAndDescricaoIgnoreCase(nome, descricao, pageable))
                .thenReturn(paginaMock);

        Page<ExameDTO> resultado = service.listar(nome, descricao, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(exameRepository).findByNomeAndDescricaoIgnoreCase(nome, descricao, pageable);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não encontrar exames")
    void deveRetornarListaVaziaQuandoNaoEncontrar() {

        String nome = "Exame Inexistente";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exame> paginaVazia = new PageImpl<>(List.of());

        when(exameRepository.findByNomeIgnoreCase(nome, pageable)).thenReturn(paginaVazia);

        Page<ExameDTO> resultado = service.listar(nome, null, pageable);

        assertNotNull(resultado);
        assertEquals(0, resultado.getTotalElements());
        assertTrue(resultado.isEmpty());
    }

    // TESTES DO MÉTODO BUSCAR POR ID

    @Test
    @DisplayName("Deve buscar exame por ID com sucesso")
    void deveBuscarPorIdComSucesso() {

        when(exameRepository.findById(1L)).thenReturn(Optional.of(exame));

        Optional<ExameDetalheDTO> resultado = service.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Hemograma Completo", resultado.get().getNome());
        assertEquals(1L, resultado.get().getPacienteId());
        verify(exameRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando exame não existe")
    void deveRetornarVazioQuandoNaoExiste() {

        when(exameRepository.findById(999L)).thenReturn(Optional.empty());


        Optional<ExameDetalheDTO> resultado = service.buscarPorId(999L);


        assertFalse(resultado.isPresent());
        verify(exameRepository, times(1)).findById(999L);
    }

    //TESTES DO MÉTODO ATUALIZAR

    @Test
    @DisplayName("Deve atualizar exame com sucesso")
    void deveAtualizarExameComSucesso() {

        when(exameRepository.findById(1L)).thenReturn(Optional.of(exame));
        when(exameRepository.save(any(Exame.class))).thenReturn(exame);

        ExameDetalheDTO resultado = service.atualizar(1L, exameUpdateDTO);

        assertNotNull(resultado);
        assertEquals("Hemograma Completo Atualizado", resultado.getNome());
        verify(exameRepository, times(1)).findById(1L);
        verify(exameRepository, times(1)).save(any(Exame.class));
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao atualizar exame inexistente")
    void deveLancarExcecaoAoAtualizarInexistente() {

        when(exameRepository.findById(999L)).thenReturn(Optional.empty());


        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.atualizar(999L, exameUpdateDTO)
        );

        assertEquals("Exame não encontrado", exception.getMessage());
        verify(exameRepository, never()).save(any(Exame.class));
    }

    // TESTES DO MÉTODO APAGAR

    @Test
    @DisplayName("Deve apagar exame com sucesso")
    void deveApagarExameComSucesso() {

        when(exameRepository.existsById(1L)).thenReturn(true);


        boolean resultado = service.apagar(1L);


        assertTrue(resultado);
        verify(exameRepository, times(1)).existsById(1L);
        verify(exameRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve retornar false ao apagar exame inexistente")
    void deveRetornarFalseAoApagarInexistente() {

        when(exameRepository.existsById(999L)).thenReturn(false);


        boolean resultado = service.apagar(999L);


        assertFalse(resultado);
        verify(exameRepository, times(1)).existsById(999L);
        verify(exameRepository, never()).deleteById(anyLong());
    }
}
