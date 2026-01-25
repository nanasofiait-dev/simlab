package com.example.simlab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
/**
 * DTO para atualização de dados de exames.
 *
 * <p>Utilizado na operação de atualização (PUT) de exames existentes.
 * Não inclui o ID pois este é fornecido como parâmetro na URL.</p>
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */
public class ExameUpdateDTO {
    /**
     * Nome do exame.
     */
    @NotNull(message = "Nome é obrigatório")
    @NotBlank(message = "Nome não pode estar vazio")
    private String nome;
    /**
     * Descrição do exame.
     */
    private String descricao;
    /**
     * Preço do exame.
     */
    private Double preco;

    public ExameUpdateDTO() {
    }

    public ExameUpdateDTO(String nome, String descricao, Double preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
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
}
