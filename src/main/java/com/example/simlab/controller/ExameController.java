package com.example.simlab.controller;

import com.example.simlab.dto.ExameDTO;
import com.example.simlab.dto.ExameDetalheDTO;
import com.example.simlab.dto.ExameUpdateDTO;
import com.example.simlab.service.ExameService;
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

/**
 * Controller REST para gerenciar operações relacionadas a exames.
 *
 * <p> Fornece endpoints para criar, listar, buscar, atualizar e apagar exames.</p>
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */
@RestController
@RequestMapping("/exames")
public class ExameController {

    private final ExameService service;

    public ExameController(ExameService service) {
        this.service = service;
    }

    /**
     * Cria um novo exame.
     *
     * @param dto Dados iniciais para criação do exame
     * @return ResponseEntity com status 201 Created e detalhes do exame criado
     */
    @Operation(summary = "Criar novo exame", description = "Cria um novo exame associado a um paciente")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Exame criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                              {
                                             "nome":"Hemograma Completo",
                                             "descricao":"Análise completa do sangue",
                                             "preco":35,
                                            "pacienteId":1
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
                                               "campos":{
                                               "nome": "Nome é obrigatório",
                                                "preco": "Preço é obrigatório",
                                                "pacienteId": "Id do paciente é obrigatório"
                                                 },
                                                 "status": "400"
                                                 }
                                            """
                            )
                    )
            ),


            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente não encontrado com o ID fornecido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                                       {
                                                          "error": "Not Found",
                                                           "message": "Paciente não encontrado com ID fornecido",
                                                           "status": "404"
                                                       }
                                                 """
                            )
                    )

            ),


            @ApiResponse(
                    responseCode = "409",
                    description = "Nome do exame já existe no sistema",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """ 
                                            {
                                            "error": "Conflict",
                                             "message": "Existe exame com esse nome",
                                            "status": "409"
                                            }
                                            
                                            """
                            )

                    ))
    })

    @PostMapping
    public ResponseEntity<ExameDetalheDTO> criar(@Valid @RequestBody ExameDTO dto) {

        ExameDetalheDTO criar = service.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(criar);
    }

    /**
     * Lista exames com filtros opcionais e paginação.
     *
     * @param nome      Nome do exame para filtrar (opcional)
     * @param descricao Descrição do exame para filtrar(opcional)
     * @param pageable  Parâmetro para paginação e organização
     * @return ResponseEntity com status 200 OK e página de exames
     */
    @Operation(summary = "Listar exames", description = "Lista todos os exames com filtros opcionais e paginação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de exames retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<Page<ExameDTO>> listar(@RequestParam(required = false) String nome, @RequestParam(required = false) String descricao, @ParameterObject Pageable pageable) {
        Page<ExameDTO> pagina = service.listar(nome, descricao, pageable);

        return ResponseEntity.ok(pagina);

    }

    /**
     * Busca um exame pelo seu identificador único.
     *
     * @param id Identificador único do exame
     * @return RespondeEntity com status 200 OK e detalhes do exame se encontrado, ou status 404 Not Found se não encontrado
     */
    @Operation(summary = "Buscar exame por ID", description = "Retorna os detalhes de um exame específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exame encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Exame não encontrado com o ID fornecido")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExameDetalheDTO> buscar(@PathVariable Long id) {

        return service.buscarPorId(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Atualiza os dados de um exame existente.
     *
     * @param id  Identificador único do exame a ser atualizado
     * @param dto Novos dados do exame
     * @return ResponseEntity com status 200 OK e detalhes do exame atualizado
     */
    @Operation(summary = "Atualizar exame", description = "Atualiza os dados de um exame existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exame atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (validação falhou)"),
            @ApiResponse(responseCode = "404", description = "Exame não encontrado com o ID fornecido")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExameDetalheDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ExameUpdateDTO dto) {
        ExameDetalheDTO atualizada = service.atualizar(id, dto);

        return ResponseEntity.ok(atualizada);
    }

    /**
     * Apaga um exame da base de dados.
     *
     * @param id Identificação única do exame a ser apagado
     * @return ResponseEntity com status 204 No Content se apagado com sucesso ou status 404 Not Found se não encontrado
     */
    @Operation(summary = "Apagar exame", description = "Remove um exame do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exame apagado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Exame não encontrado com o ID fornecido")
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
