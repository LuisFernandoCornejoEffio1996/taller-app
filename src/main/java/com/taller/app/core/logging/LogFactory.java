package com.taller.app.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fábrica de loggers reutilizable.
 */
public final class LogFactory {

    private LogFactory(){}

    public static Logger getLogger(Class<?> clazz){
        return LoggerFactory.getLogger(clazz);
    }
}
