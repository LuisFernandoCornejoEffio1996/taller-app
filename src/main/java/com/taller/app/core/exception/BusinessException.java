package com.taller.app.core.exception;

/**
 * Excepción para reglas de negocio
 */
public class BusinessException extends RuntimeException{

    public BusinessException(String message){
        super(message);
    }
}
