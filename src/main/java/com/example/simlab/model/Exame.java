package com.example.simlab.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
/**
 * Entidade que representa um exame no sistema.
 *
 *<p>Exames são cadastrados com dados do exame e associados a um paciente específico.
 * Cada exame pertence a apenas um paciente.</p>
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */
@Entity
@Table(name = "exames")
public class Exame {
    /**
     * Identificador único do exame, gerado automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Nome do exame.
     */
    @NotNull(message = "Nome é obrigatório")
    @NotBlank(message = "Nome não pode estar vazio")
    @Column(unique = true, nullable = false,length = 100)
    private String nome;

    /**
     * Descrição do exame.
     */
    @Column(nullable = false, length = 100)
    private String descricao;

    /**
     * Preço do exame.
     */
    @Column(nullable = false)
    private Double  preco;
    /**
     * Paciente ao qual o exame pertence.
     *
     * <p>Relacionamento obrigatório: cada exame deve estar associado a um paciente.</p>
     */
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    public Exame() {
    }

    public Exame(String nome, String descricao, Double preco, Paciente paciente) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.paciente = paciente;
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

    public void setPreco(Double  preco) {
        this.preco = preco;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
