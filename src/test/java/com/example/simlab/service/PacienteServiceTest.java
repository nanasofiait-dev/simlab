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
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do PacienteService")
class PacienteServiceTest {

    @Mock
    private PacienteRepository repository;

    @InjectMocks
    private PacienteService service;
    private PacienteDTO pacienteDTO;
    private PacienteUpdateDTO pacienteUpdateDTO;
    private Paciente paciente;

    @BeforeEach
    void setUp() {
        // Preparar dados de teste (reutilizados em vários testes)
        pacienteDTO = new PacienteDTO(
                "Maria Silva",
                LocalDate.of(1985, 3, 15),
                "12345678",
                "912345678",
                "maria@email.com"
        );

        pacienteUpdateDTO = new PacienteUpdateDTO(
                "Maria Silva Santos",
                LocalDate.of(1985, 3, 15),
                "12345678",
                "919999999",
                "maria.nova@email.com"
        );

        paciente = new Paciente(
                "Maria Silva",
                LocalDate.of(1985, 3, 15),
                "12345678",
                "912345678",
                "maria@email.com"
        );
        paciente.setId(1L);
    }

    @Test
    @DisplayName("Deve criar paciente com sucesso")
    void deveCriarPacienteComSucesso() {

        when(repository.existsByCartaoCidadaoIgnoreCase(pacienteDTO.getCartaoCidadao()))
                .thenReturn(false); // Simula que CC não existe

        when(repository.save(any(Paciente.class)))
                .thenReturn(paciente); // Simula que salvou

        PacienteDetalheDTO resultado = service.criar(pacienteDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Maria Silva", resultado.getNome());
        assertEquals("12345678", resultado.getCartaoCidadao());

        verify(repository, times(1)).existsByCartaoCidadaoIgnoreCase("12345678");
        verify(repository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar DuplicadoException quando CC já existe")
    void deveLancarExcecaoQuandoCCDuplicado() {

        when(repository.existsByCartaoCidadaoIgnoreCase(pacienteDTO.getCartaoCidadao()))
                .thenReturn(true); // Simula que CC JÁ existe


        DuplicadoException exception = assertThrows(
                DuplicadoException.class,
                () -> service.criar(pacienteDTO)
        );

        assertEquals("Não é possível cadastrar paciente, pois já existe paciente com este Cartão Cidadão",
                exception.getMessage());

        verify(repository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve buscar paciente por ID com sucesso")
    void deveBuscarPorIdComSucesso() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(paciente));

        Optional<PacienteDetalheDTO> resultado = service.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Maria Silva", resultado.get().getNome());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando paciente não existe")
    void deveRetornarVazioQuandoNaoExiste() {
        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        Optional<PacienteDetalheDTO> resultado = service.buscarPorId(999L);

        assertFalse(resultado.isPresent());
        verify(repository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve atualizar paciente com sucesso")
    void deveAtualizarPacienteComSucesso() {
        when(repository.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(repository.save(any(Paciente.class)))
                .thenReturn(paciente);

        PacienteDetalheDTO  resultado = service.atualizar(1L, pacienteUpdateDTO);

        assertNotNull(resultado);
        assertEquals("Maria Silva Santos", resultado.getNome());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao atualizar paciente inexistente")
    void deveLancarExcecaoAoAtualizarInexistente() {

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.atualizar(999L, pacienteUpdateDTO)
        );

        assertEquals("Paciente não foi encontrado", exception.getMessage());
        verify(repository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve apagar paciente com sucesso")
    void deveApagarPacienteComSucesso() {

        when(repository.existsById(1L))
                .thenReturn(true);


        boolean resultado = service.apagar(1L);

        assertTrue(resultado);
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve retornar false ao apagar paciente inexistente")
    void deveRetornarFalseAoApagarInexistente() {

        when(repository.existsById(999L))
                .thenReturn(false);

        boolean resultado = service.apagar(999L);

        assertFalse(resultado);
        verify(repository, times(1)).existsById(999L);
        verify(repository, never()).deleteById(anyLong());
    }
}
