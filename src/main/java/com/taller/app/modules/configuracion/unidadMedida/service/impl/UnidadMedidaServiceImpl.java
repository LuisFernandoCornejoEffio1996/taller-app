package com.taller.app.modules.configuracion.unidadMedida.service.impl;

import com.taller.app.core.audit.AuditAction;
import com.taller.app.core.audit.Auditable;
import com.taller.app.core.logging.LogFactory;
import com.taller.app.events.UnidadMedidaActualizadaEvent;
import com.taller.app.modules.configuracion.unidadMedida.entity.UnidadMedida;
import com.taller.app.modules.configuracion.unidadMedida.repository.UnidadMedidaRepository;
import com.taller.app.modules.configuracion.unidadMedida.service.UnidadMedidaService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementacion del servicio de configuracion de unidad de medida
 */
@Service
@RequiredArgsConstructor
public class UnidadMedidaServiceImpl implements UnidadMedidaService {

    private static final Logger log = LogFactory.getLogger(UnidadMedidaServiceImpl.class);

    private final UnidadMedidaRepository repository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<UnidadMedida> listar() {
        log.info("Listando todas las unidades de medida");
        return repository.findAll();
    }

    @Override
    public List<UnidadMedida> listarActivos() {
        log.info("Listando unidades de medida activas");
        return repository.findByEstado(1);
    }

    @Override
    public Optional<UnidadMedida> buscarPorId(Integer id) {
        log.info("Buscando unidad de medida ID={}", id);
        return repository.findById(id);
    }

    /**
     * Crea o actualiza una unidad de medida.
     * Si existe una unidad con la misma abreviatura y está inactiva → se reactiva.
     * Si existe activa → error.
     */
    @Override
    @Transactional
    @Auditable(
            accion = AuditAction.ACTUALIZAR,
            entidad = "UNIDAD_MEDIDA"
    )
    public UnidadMedida crearOActualizar(UnidadMedida unidad) {

        log.info("Procesando creación/actualización de unidad de medida: {}", unidad.getAbreviatura());

        Optional<UnidadMedida> existente = repository.findByAbreviaturaIgnoreCase(unidad.getAbreviatura());

        UnidadMedida saved;

        if (existente.isPresent()) {
            UnidadMedida u = existente.get();

            // Si existe pero está inactiva → reactivar
            if (u.getEstado() == 0) {
                log.info("Reactivando unidad de medida inactiva: {}", u.getAbreviatura());

                u.setEstado(1);
                u.setNombre(unidad.getNombre());
                u.setFechaModificacion(LocalDateTime.now());
                u.setModificadoPor(1);

                saved = repository.save(u);

                // 🔥 LIMPIAR CONTEXTO
                entityManager.flush();
                entityManager.clear();

                publisher.publishEvent(new UnidadMedidaActualizadaEvent(this, saved.getIdUnidad()));

                log.info("Unidad de medida reactivada ID={}", saved.getIdUnidad());
                return saved;
            }

            // Si existe activa → duplicado
            log.warn("Intento de duplicar unidad de medida activa: {}", u.getAbreviatura());
            throw new IllegalStateException("La abreviatura ya existe y está activa.");
        }

        // Crear nueva unidad
        log.info("Creando nueva unidad de medida: {}", unidad.getAbreviatura());

        unidad.setEstado(1);
        unidad.setFechaCreacion(LocalDateTime.now());
        unidad.setCreadoPor(1);

        saved = repository.save(unidad);

        // 🔥 LIMPIAR CONTEXTO
        entityManager.flush();
        entityManager.clear();

        log.info("Unidad de medida creada ID={}", saved.getIdUnidad());

        // 🔥 Notificar a toda la aplicación
        publisher.publishEvent(new UnidadMedidaActualizadaEvent(this, saved.getIdUnidad()));

        return saved;
    }

    /**
     * Desactiva una unidad de medida (no se elimina).
     */
    @Override
    @Transactional
    @Auditable(
            accion = AuditAction.ELIMINAR,
            entidad = "UNIDAD_MEDIDA"
    )
    public void desactivar(Integer id) {
        log.info("Desactivando unidad de medida ID={}", id);

        repository.findById(id).ifPresent(u -> {
            u.setEstado(0);
            u.setFechaModificacion(LocalDateTime.now());
            u.setModificadoPor(1);
            repository.save(u);

            log.info("Unidad de medida desactivada ID={}", id);

            // 🔥 Notificar a toda la aplicación
            publisher.publishEvent(new UnidadMedidaActualizadaEvent(this, id));
        });
    }


    @Override
    public boolean existeNombre(String nombre) {
        return repository.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existeAbreviatura(String abreviatura) {
        return repository.existsByAbreviaturaIgnoreCase(abreviatura);
    }
}


