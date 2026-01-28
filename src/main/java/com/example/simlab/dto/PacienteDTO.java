package com.example.simlab.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
/**
 * DTO para criação e listagem de paciente.
 *
 * <p>Utilizado nas operações de criação (POST) e listagem (GET) de pacientes
 * Não inclui o ID pois este é gerado automaticamente pelo sistema</p>
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */

public class PacienteDTO {
    /**
     * Nome completo do paciente.
     */
    @NotNull(message = "Nome é obrigatório")
    @NotBlank(message = "Nome não pode estar vazio")
    private String nome;

    /**
     * Data de nascimento do paciente.
     */
    @NotNull(message = "Data de nascimento é obrigatória")
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
     * Endereço de e-mail do paciente.
     */
    private String email;

    public PacienteDTO() {
    }

    public PacienteDTO(String nome, LocalDate dataDeNascimento, String cartaoCidadao, String telefone, String email) {
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.cartaoCidadao = cartaoCidadao;
        this.telefone = telefone;
        this.email=email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
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
}
