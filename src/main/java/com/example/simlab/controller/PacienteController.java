package com.example.simlab.controller;

import com.example.simlab.dto.PacienteDTO;
import com.example.simlab.dto.PacienteDetalheDTO;
import com.example.simlab.dto.PacienteUpdateDTO;
import com.example.simlab.service.ExameService;
import com.example.simlab.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller REST para gerenciar operações relacionadas a pacientes.
 *
 * <p> Fornece endpoints para criar, listar, buscar, atualizar e apagar pacientes.</p>
 *
 * @author  Amanda
 * @version 1.0
 * @since 2026-01-15
 */
@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    /**
     * Cria um novo paciente.
     *
     * @param dto Dados do paciente a ser criado
     * @return ResponseEntity com status 201 Created e detalhes do paciente criado
     */

    @PostMapping
    public ResponseEntity<PacienteDetalheDTO> criar(@Valid @RequestBody PacienteDTO dto) {
        PacienteDetalheDTO criar = service.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(criar);
    }

    /**
     * Lista pacientes com filtros opcionais e paginação.
     *
     * @param nome Nome do paciente para filtrar (opcional)
     * @param dataDeNascimento Data de Nascimento do paciente para filtrar (opcional)
     * @param cartaoCidadao  Cartão de Cidadão para filtrar (opcional)
     * @param pageable Parâmetros de paginação e ordenação
     * @return  ResponseEntity com status 200 OK e página de pacientes
     */
    @GetMapping
    public ResponseEntity<Page<PacienteDTO>> listar(@RequestParam(required = false) String nome,
                                                    @RequestParam(required = false) LocalDate dataDeNascimento,
                                                    @RequestParam(required = false) String cartaoCidadao,
                                                    Pageable pageable) {

        Page<PacienteDTO> pagina = service.listar(nome, dataDeNascimento, cartaoCidadao, pageable);

        return ResponseEntity.ok(pagina);

    }

    /**
     * Busca um paciente pelo seu identificador único.
     *
     * @param id Identificador único do paciente
     * @return ResponseEntity com status 200 OK e detalhes do paciente se encontrado
     * ou status 404 Not Found se não encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDetalheDTO> buscar(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map( dto -> ResponseEntity.ok(dto))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    /**
     * Atualiza os dados de um paciente existente.
     *
     * @param id Identificador único do paciente a ser atualizado
     * @param dto Novos dados do paciente
     * @return ResponseEntity com status 200 OK e detalhes do paciente atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<PacienteDetalheDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PacienteUpdateDTO dto) {

        PacienteDetalheDTO atualizada = service.atualizar(id, dto);

        return ResponseEntity.ok(atualizada);

    }

    /**
     * Apaga um paciente da base de dados.
     *
     * @param id Identificador único do paciente a ser apagado
     * @return ResponseEntity com status 204 No Content se apagado com sucesso ou status 404 Not Found se não encontrado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar (@PathVariable Long id){

        if (service.apagar(id)){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
