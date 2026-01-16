package com.example.simlab.exception;

//Será usada quando recurso não existe (404)
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

}
