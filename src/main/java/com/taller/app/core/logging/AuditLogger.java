package com.taller.app.core.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logger dedicado a auditoria
 */
public class AuditLogger {

    public static final Logger auditLogger = LoggerFactory.getLogger("AUDIT_LOGGER");

    private AuditLogger() {}

    public static void log(Integer usuarioId, String modulo, String accion, String descripcion) {

        auditLogger.info(
                "usuario={} modulo={} accion={} detalle={}",
                usuarioId,
                modulo,
                accion,
                descripcion
        );

    }

}

