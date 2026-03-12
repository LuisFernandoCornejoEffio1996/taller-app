package com.taller.app.core.logging;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de registrar auditoría de acciones.
 */
@Service
@RequiredArgsConstructor
public class AuditService {

    private static final Logger log = LogFactory.getLogger(AuditService.class);

    public void registrar(String modulo, String accion, Integer usuarioId, String descripcion) {

        log.info(
                "AUDIT | modulo={} accion={} usuario={} detalle={}",
                modulo,
                accion,
                usuarioId,
                descripcion
        );

    }

}
