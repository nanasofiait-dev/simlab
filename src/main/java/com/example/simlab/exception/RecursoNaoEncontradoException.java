package com.example.simlab.exception;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado.
 *
 *  <p>Utilizada quando há tentativa de buscar, atualizar, apagar paciente ou exame
 *  que não existe no sistema.</p>
 *
 *  @author Amanda
 *  @version 1.0
 *  @since 2026-01-15
 */

//Será usada quando recurso não existe (404)
public class RecursoNaoEncontradoException extends RuntimeException {
    /**
     * Cria uma exceção com mensagem específica.
     *
     * @param mensagem Mensagem de erro descrevendo o recurso não encontrado
     */

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

}
