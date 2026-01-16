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

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    //criar, listar,buscar,atualizar e deletar

    @PostMapping
    public ResponseEntity<PacienteDetalheDTO> criar(@Valid @RequestBody PacienteDTO dto) {
        PacienteDetalheDTO criar = service.criar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(criar);
    }

    @GetMapping
    public ResponseEntity<Page<PacienteDTO>> listar(@RequestParam(required = false) String nome,
                                                    @RequestParam(required = false) LocalDate dataDeNascimento,
                                                    @RequestParam(required = false) String cartaoCidadao,
                                                    Pageable pageable) {

        Page<PacienteDTO> pagina = service.listar(nome, dataDeNascimento, cartaoCidadao, pageable);

        return ResponseEntity.ok(pagina);

    }


    @GetMapping("/{id}")
    public ResponseEntity<PacienteDetalheDTO> buscar(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map( dto -> ResponseEntity.ok(dto))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDetalheDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PacienteUpdateDTO dto) {

        PacienteDetalheDTO atualizada = service.atualizar(id, dto);

        return ResponseEntity.ok(atualizada);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagar (@PathVariable Long id){

        if (service.apagar(id)){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
