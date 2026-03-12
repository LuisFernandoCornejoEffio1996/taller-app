package com.taller.app.core.util;

/**
 * Utilidades de validación
 */
public class ValidationUtil {

    private ValidationUtil(){}

    public static boolean isEmpty(String value){
        return value == null || value.trim().isEmpty();
    }
}
