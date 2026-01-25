package com.example.simlab.exception;

/**
 * Exceção lançada quando se tenta criar um recurso duplicado.
 *
 *  <p>Utilizada quando há tentativa de cadastrar paciente com Cartão de Cidadão já existente
 *  ou exame com nome já cadastrado no sistema.</p>
 *
 *  @author Amanda
 *  @version 1.0
 *  @since 2026-01-15
 *
 */
//Será usada para valores duplicados (400)
public class DuplicadoException extends RuntimeException{
    /**
     * Cria uma exceção com mensagem específica.
     *
     * @param mensagem Mensagem de erro descrevendo a duplicação
     */

    public  DuplicadoException (String mensagem ){
        super(mensagem);
    }

}
