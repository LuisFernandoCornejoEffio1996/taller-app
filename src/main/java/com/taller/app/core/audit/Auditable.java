package com.taller.app.core.audit;

import java.lang.annotation.*;

/**
 * Anotacion que permite marcar métodos que deben ser auditados automáticamente.
 */
 @Target(ElementType.METHOD)
 @Retention(RetentionPolicy.RUNTIME)
 @Documented
public @interface Auditable {

    /**
     * Acción que se ejecuta sobre la entidad
     */
    AuditAction accion();

    /**
     * Nombre de la entidad
     */
    String entidad();
}
