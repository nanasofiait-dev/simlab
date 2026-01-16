package com.example.simlab.exception;

//Será usada para valores duplicados (400)
public class DuplicadoException extends RuntimeException{

    public  DuplicadoException (String mensagem ){
        super(mensagem);
    }

}
