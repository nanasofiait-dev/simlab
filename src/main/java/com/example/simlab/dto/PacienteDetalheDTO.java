package com.example.simlab.dto;

import java.time.LocalDate;

/**
 * DTO para retorno detalhado de pacientes.
 *
 * <p>Utilizado como resposta nas operações de criação, atualização e busca individual.
 *  Inclui o ID gerado pelo sistema.</p>
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */
public class PacienteDetalheDTO {
    /**
     * Identificador único do paciente.
     */
    private Long id;
    /**
     * Nome completo do paciente
     */
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


    public PacienteDetalheDTO() {
    }

    public PacienteDetalheDTO(Long id, String nome, LocalDate dataDeNascimento, String cartaoCidadao, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.cartaoCidadao = cartaoCidadao;
        this.telefone = telefone;
        this.email = email;
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
