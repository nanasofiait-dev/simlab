package com.example.simlab.controller;

import com.example.simlab.dto.PacienteDTO;
import com.example.simlab.dto.PacienteDetalheDTO;
import com.example.simlab.dto.PacienteUpdateDTO;
import com.example.simlab.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
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
 * @author Amanda
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
    @Operation(summary = "Criar novo paciente", description = "Cria um novo paciente no sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Paciente criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "nome": "Maria Silva",
                                              "dataDeNascimento": "1990-01-15",
                                              "cartaoCidadao": "12345678",
                                              "telefone": "912345678",
                                              "email": "maria@email.com"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de entrada inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                            "error": "Bad Request",
                                                            "message": "Erro de validação",
                                                            "campos": {
                                                              "dataDeNascimento": "Data de nascimento é obrigatória",
                                                              "nome": "Nome é obrigatório",
                                                              "telefone": "Telefone deve ter 9 dígitos começando com 9",
                                                              "cartaoCidadao": "Cartão de Cidadão deve ter exatamente 8 dígitos"
                                                            },
                                                             "status": 400
                                                          }
                                            """
                            )

                    )

            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Cartão de Cidadão já existe",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            { "error" :" Conflict",
                                              "message": "Não é possível cadastrar paciente, pois já existe paciente com este Cartão Cidadão"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<PacienteDetalheDTO> criar(@Valid @RequestBody PacienteDTO dto) {
        PacienteDetalheDTO criado = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    /**
     * Lista pacientes com filtros opcionais e paginação.
     *
     * @param nome             Nome do paciente para filtrar (opcional)
     * @param dataDeNascimento Data de Nascimento do paciente para filtrar (opcional)
     * @param cartaoCidadao    Cartão de Cidadão para filtrar (opcional)
     * @param pageable         Parâmetros de paginação e ordenação
     * @return ResponseEntity com status 200 OK e página de pacientes
     */
    @Operation(summary = "Listar pacientes", description = "Lista todos os pacientes com filtros opcionais e paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<PacienteDTO>> listar(@RequestParam(required = false) String nome,
                                                    @RequestParam(required = false) LocalDate dataDeNascimento,
                                                    @RequestParam(required = false) String cartaoCidadao,
                                                    @ParameterObject Pageable pageable) {

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
    @Operation(summary = "Buscar paciente por ID", description = "Retorna os detalhes de um paciente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado com o ID fornecido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDetalheDTO> buscar(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map(dto -> ResponseEntity.ok(dto))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    /**
     * Atualiza os dados de um paciente existente.
     *
     * @param id  Identificador único do paciente a ser atualizado
     * @param dto Novos dados do paciente
     * @return ResponseEntity com status 200 OK e detalhes do paciente atualizado
     */
    @Operation(summary = "Atualizar paciente", description = "Atualiza os dados de um paciente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (validação falhou)"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado com o ID fornecido")
    })
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
    @Operation(summary = "Apagar paciente", description = "Remove um paciente do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente apagado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado com o ID fornecido")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {

        if (service.apagar(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
