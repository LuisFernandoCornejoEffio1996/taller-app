package com.taller.app.core.exception;

/**
 * Excepción para errores técnicos (BD, IO, etc)
 */
public class InfrastructureException extends RuntimeException{

    public InfrastructureException(String message, Throwable cause){
        super(message, cause);
    }
}
