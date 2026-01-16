package com.example.simlab.controller;

import com.example.simlab.dto.ExameDTO;
import com.example.simlab.dto.ExameDetalheDTO;
import com.example.simlab.dto.ExameUpdateDTO;
import com.example.simlab.service.ExameService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exames")
public class ExameController {

    private final ExameService service;

    public ExameController(ExameService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<ExameDetalheDTO> criar(@Valid @RequestBody ExameDTO dto) {

        ExameDetalheDTO criar = service.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(criar);
    }

    @GetMapping
    public ResponseEntity<Page<ExameDTO>> listar(@RequestParam(required = false) String nome, @RequestParam(required = false) String descricao, Pageable pageable) {
        Page<ExameDTO> pagina = service.listar(nome, descricao, pageable);

        return ResponseEntity.ok(pagina);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ExameDetalheDTO> buscar(@PathVariable Long id) {

        return service.buscarPorId(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExameDetalheDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ExameUpdateDTO dto) {
        ExameDetalheDTO atualizada = service.atualizar(id,dto);

        return ResponseEntity.ok(atualizada);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        if (service.apagar(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
