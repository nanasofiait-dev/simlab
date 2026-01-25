package com.example.simlab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para criação e listagem do exame.
 *
 * <p>Utilizado nas operações de criação (POST) e listagem (GET) de exames.
 *  Não inclui o ID pois este é gerado automaticamente pelo sistema.</p>
 *
 *  @author Amanda
 *  @version 1.0
 *  @since 2026-01-15
 */
public class ExameDTO {
    /**
     * Nome do exame.
     */
    @NotNull(message = "Nome é obrigatório")
    @NotBlank(message = "Nome não pode estar vazio")
    private String nome;
    /**
     * Descrição do exame.
     */
    @NotNull(message = "Descrição é obrigatória")
    private String descricao;
    /**
     * Preço do exame.
     */
    @NotNull(message = "Valor é obrigatório")
    private Double preco;
    /**
     * Identificador do paciente ao qual o exame pertence.
     */
    @NotNull(message = "Paciente é obrigatório")  // ← NOVO!
    private Long pacienteId;

    public ExameDTO() {
    }

    public ExameDTO(String nome, String descricao, Double preco, Long pacienteId) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.pacienteId=pacienteId;

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }
}
