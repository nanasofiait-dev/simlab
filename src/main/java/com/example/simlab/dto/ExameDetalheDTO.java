package com.example.simlab.dto;

/**
 * DTO para retorno detalhado de exames.
 *
 * <p>Utilizado como resposta nas operações de criação, atualização e busca individual.
 * Inclui o ID gerado pelo sistema.</p>
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */
public class ExameDetalheDTO {
    /**
     * Identificador único do exame.
     */
    private Long id;
    /**
     * Nome  do exame.
     */
    private String nome;
    /**
     * Descrição do exame.
     */
    private String descricao;
    /**
     * Preço do exame.
     */
    private Double preco;
    /**
     * Identificador do paciente ao qual o exame pertence.
     */
    private Long pacienteId;

    public ExameDetalheDTO() {
    }

    public ExameDetalheDTO(Long id, String nome, String descricao, Double preco, Long pacienteId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.pacienteId = pacienteId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
