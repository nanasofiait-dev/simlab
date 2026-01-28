package com.example.simlab.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um paciente no sistema.
 *
 * <p>Pacientes são cadastrados com dados pessoais únicos (Cartão de Cidadão)
 * e podem ter múltiplos exames associados.</p>
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */
@Entity
@Table(name = "pacientes")
public class Paciente {
    /**
     * Identificador único do paciente, gerado automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do paciente.
     */
    @NotNull(message = "Nome é obrigatório")
    @NotBlank(message = "Nome não pode estar vazio")
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Data de nascimento do paciente.
     */
    @NotNull(message = "Data de nascimento é obrigatória")
    @Column(name = "data_nascimento")
    private LocalDate dataDeNascimento;
    /**
     * Número do Cartão de Cidadão do paciente.
     *
     * <p>Campo único no sistema - não pode haver dois pacientes com o mesmo Cartão de Cidadão.</p>
     */
    @NotBlank(message = "Número do CC é obrigatório")
    @Pattern(regexp = "^[0-9]{8}$",message = "Cartão de Cidadão deve ter exatamente 8 dígitos")
    @Column(name="cartao_cidadao",nullable = false, unique = true,length = 8)
    private String cartaoCidadao;
    /**
     * Telefone do paciente.
     */
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "^9[0-9]{8}$", message = "Telefone deve ter 9 dígitos começando com 9")
    @Column(length = 9)
    private String telefone;
    /**
     * Endereço de email do paciente.
     */
    @Column(length = 100)
    private String email;
    /**
     * Lista de exames associados a este paciente.
     *
     * <p>Relacionamento bidirecional: um paciente pode ter múltiplos exames.</p>
     */
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL)
    private List<Exame> exames= new ArrayList<>();

    public Paciente() {
    }


    public Paciente(String nome, LocalDate dataDeNascimento, String cartaoCidadao, String telefone, String email) {
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.cartaoCidadao = cartaoCidadao;
        this.telefone = telefone;
        this.email = email;

    }


    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
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

    public String getCartaoCidadao() {
        return cartaoCidadao;
    }

    public void setCartaoCidadao(String cartaoCidadao) {
        this.cartaoCidadao = cartaoCidadao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Exame> getExames() {
        return exames;
    }

    public void setExames(List<Exame> exames) {
        this.exames = exames;
    }
}
