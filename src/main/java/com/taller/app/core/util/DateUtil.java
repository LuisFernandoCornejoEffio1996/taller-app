package com.taller.app.core.util;

import java.time.LocalDateTime;

/**
 * Utilidades para manejo de fechas
 */
public class DateUtil {

    private DateUtil(){}

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
