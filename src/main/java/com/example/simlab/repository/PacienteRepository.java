package com.example.simlab.repository;

import com.example.simlab.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

/**
 * Repository para acesso aos dados de pacientes.
 *
 * <p>Fornece métodos de consulta customizados além dos métodos padrão do JpaRepository.</p>
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Busca paciente por nome com paginação.
     *
     * @param nome Nome do paciente
     * @param pageable Configuração de paginação e ordenação
     * @return Página de pacientes com o nome especificado
     */
    Page<Paciente> findByNomeIgnoreCase (String nome, Pageable pageable);

    /**
     * Busca pacientes por data de nascimento com paginação.
     *
     * @param dataDeNascimento Data de nascimento do paciente
     * @param pageable Configurção de paginação e ordenação
     * @return Página de pacientes com a  Data de Nascimento especificada
     */
    Page<Paciente>findByDataDeNascimento (LocalDate dataDeNascimento,Pageable pageable);

    /**
     * Busca paciente por Cartão de Cidadão com paginação.
     *
     * @param cartaoCidadao Cartão de Cidadao do paciente
     * @param pageable Configuração de paginação e ordenação.
     * @return Página de paciente encontrado com o Cartão de Cidadão especificado
     */
    Page<Paciente> findByCartaoCidadaoIgnoreCase (String cartaoCidadao, Pageable pageable);

    /**
     * Busca pacientes por nome e Cartão de Cidadão com paginação.
     *
     * @param nome Nome do paciente
     * @param cartaoCidadao Cartão de Cidadão do paciente
     * @param pageable Configuração de página e ordenação
     * @return Página de pacientes que correspondem a ambos critérios.
     */
    Page<Paciente>findByNomeAndCartaoCidadao ( String nome, String cartaoCidadao, Pageable pageable);

    /**
     * Verifica se existe paciente com o Cartão de Cidadão especificado
     *
     * <p>Busca case-sensitive para evitar duplicações com diferenças de maiúscula/minúsculas. </p>
     *
     * @param cartaoCidadao Cartão de Cidadão a verificar
     * @return True se existe paciente com este Cartão de Cidadão, false caso contrário
     */
    boolean existsByCartaoCidadaoIgnoreCase ( String cartaoCidadao);

    /**
     * Busca pacientes por nome E Cartão de Cidadão com paginação.
     *
     * <p>Busca case-insensitive em ambos os campos.</p>
     *
     * @param nome Nome do paciente
     * @param cartaoCidadao Cartão de Cidadão do paciente
     * @param pageable Configuração de paginação e ordenação
     * @return Página de pacientes que correspondem a ambos os critérios
     */
    Page<Paciente> findByNomeIgnoreCaseAndCartaoCidadaoIgnoreCase(String nome, String cartaoCidadao, Pageable pageable);



}
