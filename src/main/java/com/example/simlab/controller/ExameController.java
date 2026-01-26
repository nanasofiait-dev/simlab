package com.example.simlab.controller;

import com.example.simlab.dto.ExameDTO;
import com.example.simlab.dto.ExameDetalheDTO;
import com.example.simlab.dto.ExameUpdateDTO;
import com.example.simlab.service.ExameService;
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
 * @author  Amanda
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
    @PostMapping
    public ResponseEntity<ExameDetalheDTO> criar(@Valid @RequestBody ExameDTO dto) {

        ExameDetalheDTO criar = service.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(criar);
    }

    /**
     * Lista exames com filtros opcionais e paginação.
     *
     * @param nome Nome do exame para filtrar (opcional)
     * @param descricao Descrição do exame para filtrar(opcional)
     * @param pageable Parâmetro para paginação e organização
     * @return ResponseEntity com status 200 OK e página de exames
     */
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
    @GetMapping("/{id}")
    public ResponseEntity<ExameDetalheDTO> buscar(@PathVariable Long id) {

        return service.buscarPorId(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Atualiza os dados de um exame existente.
     *
     * @param id Identificador único do exame a ser atualizado
     * @param dto Novos dados do exame
     * @return ResponseEntity com status 200 OK e detalhes do exame atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExameDetalheDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ExameUpdateDTO dto) {
        ExameDetalheDTO atualizada = service.atualizar(id,dto);

        return ResponseEntity.ok(atualizada);
    }

    /**
     * Apaga um exame da base de dados.
     *
     * @param id Identificação única do exame a ser apagado
     * @return ResponseEntity com status 204 No Content se apagado com sucesso ou status 404 Not Found se não encontrado
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (service.apagar(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
