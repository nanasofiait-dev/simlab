package com.example.simlab.repository;

import com.example.simlab.model.Exame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository para acesso aos dados de exames.
 *
 * <p>Fornece métodos de consulta customizados além dos métodos padrão do JpaRepository.</p>
 *
 * @author Amanda
 * @version 1.0
 * @since 2026-01-15
 */
public interface ExameRepository extends JpaRepository<Exame,Long> {
    /**
     * Busca exame por nome com paginação.
     *
     *<p>Busca exata e case-sensitive (não diferencia maiúsculas/minúsculas).</p>
     *
     * @param nome Nome do exame
     * @param pageable Configuração de página e ordenação
     * @return Página de exames com o nome especificado
     */
    Page<Exame> findByNomeIgnoreCase (String nome, Pageable pageable);

    /**
     * Verifica se existe exame com o nome especificado.
     *
     * <p>Utilizado para validar duplicação de nomes de exames.</p>
     *
     * @param nome Nome do exame
     * @return True se existir o nome exame com o nome especificado, false caso não exista
     */
    boolean existsByNome(String nome);

    /**
     * Busca exame por descrição com paginação.
     *
     * <p>Busca parcial (contém texto) e case-insensitive.</p>
     *
     * @param descricao Descrição do exame
     * @param pageable Configuração de paginação e ordenação
     * @return Página de exame com a descrição especificado
     */
    Page<Exame> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);

    /**
     * Busca exame por nome e descrição com paginação.
     *
     * <p>Busca exata em ambos os campos, case-insensitive.</p>
     *
     * @param nome Nome do exame
     * @param descricao Descrição do exame
     * @param pageable Configuração de paginação e ordenação
     * @return Página de exame com nome e descrição especificado
     */
    Page<Exame> findByNomeAndDescricaoIgnoreCase (String nome, String descricao, Pageable pageable);


}
