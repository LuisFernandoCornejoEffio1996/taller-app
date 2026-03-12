package com.taller.app.modules.configuracion.empresa.service.impl;

import com.taller.app.core.audit.AuditAction;
import com.taller.app.core.audit.Auditable;
import com.taller.app.core.logging.LogFactory;
import com.taller.app.events.EmpresaActualizadaEvent;
import com.taller.app.modules.configuracion.empresa.entity.EmpresaConfig;
import com.taller.app.modules.configuracion.empresa.repository.EmpresaConfigRepository;
import com.taller.app.modules.configuracion.empresa.service.EmpresaConfigService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Implementacion del servicio de configuracion de empresa
 */
@Service
@RequiredArgsConstructor
public class EmpresaConfigServiceImpl implements EmpresaConfigService {

    private static final Logger log = LogFactory.getLogger(EmpresaConfigServiceImpl.class);

    private final EmpresaConfigRepository repository;
    private final ApplicationEventPublisher publisher;

    /**
     * Obtiene la configuracion de empresa
     */
    @Override
    public EmpresaConfig obtenerEmpresa(){
        log.info("Consultando configuracion de empresa");
        return repository.obtenerEmpresaActiva().orElse(null);
    }

    /**
     * guarda o actualiza la informacion de configuracion de la empresa
     */
    @Override
    @Auditable(
            accion = AuditAction.ACTUALIZAR,
            entidad = "EMPRESA_CONFIG"
    )
    public EmpresaConfig guardar(EmpresaConfig empresa){
        log.info("Actualizando configuración de empresa");

        empresa.setUsuarioModificacion(1);
        empresa.setFechaModificacion(LocalDateTime.now());

        if (empresa.getFechaCreacion() == null) {
            empresa.setFechaCreacion(LocalDateTime.now());
        }

        EmpresaConfig saved = repository.save(empresa);

        log.info("Configuración de empresa actualizada ID={}", saved.getIdEmpresaConfig());

        // 🔥 Notificar a toda la aplicación que el nombre cambió
        publisher.publishEvent(new EmpresaActualizadaEvent(this, saved.getNombreComercial()));

        return saved;

    }
}
