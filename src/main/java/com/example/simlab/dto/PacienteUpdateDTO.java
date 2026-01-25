package com.example.simlab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO para atualização de dados de pacientes.
 *
 * <p>Utilizado na operação de atualização (PUT) de pacientes existentes.
 * Não inclui o ID pois este é fornecido como parâmetro na URL.</p>
 *
 *@author Amanda
 *@version 1.0
 *@since 2026-01-15
 */
public class PacienteUpdateDTO {
    /**
     * Nome completo do paciente.
     */
    @NotBlank
    private String nome;
    /**
     * Data de nascimento do paciente.
     */
    private LocalDate dataDeNascimento;
    /**
     * Número do Cartão de Cidadão do paciente.
     */
    private String cartaoCidadao;
    /**
     * Número de telefone do paciente.
     */
    private String telefone;
    /**
     * Endereço de e-mail do paciente.
     */
    private String email;

    public PacienteUpdateDTO() {
    }

    public PacienteUpdateDTO(String nome, LocalDate dataDeNascimento, String cartaoCidadao, String telefone, String email) {
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.cartaoCidadao = cartaoCidadao;
        this.telefone = telefone;
        this.email = email;
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
