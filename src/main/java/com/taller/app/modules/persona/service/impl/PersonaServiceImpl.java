package com.taller.app.modules.persona.service.impl;

import com.taller.app.core.audit.AuditAction;
import com.taller.app.core.audit.Auditable;
import com.taller.app.core.logging.LogFactory;
import com.taller.app.events.PersonaActualizadaEvent;
import com.taller.app.modules.persona.entity.Persona;
import com.taller.app.modules.persona.entity.TipoDocumento;
import com.taller.app.modules.persona.entity.TipoPersona;
import com.taller.app.modules.persona.repository.PersonaRepository;
import com.taller.app.modules.persona.service.PersonaService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Implementación del servicio de gestión de personas.
 *
 * Este servicio aplica TODA la lógica de negocio:
 *  - Reutilización automática por documento
 *  - Validaciones NATURAL / JURIDICA
 *  - Generación de nombre_completo
 *  - Auditoría
 *  - Desactivación inteligente
 */
@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private static final Logger log = LogFactory.getLogger(PersonaServiceImpl.class);

    private final PersonaRepository repository;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private EntityManager entityManager;

    // ============================================================
    // MÉTODO PRINCIPAL: OBTENER O CREAR PERSONA
    // ============================================================
    @Override
    @Transactional
    @Auditable(
            accion = AuditAction.CREAR,
            entidad = "PERSONA"
    )
    public Persona obtenerOCrear(Persona persona, Integer idUsuario) {

        log.info("Procesando creación/reutilización de persona con documento {}", persona.getNumeroDocumento());

        // 1. Buscar por documento
        Optional<Persona> existente = repository.findByNumeroDocumento(persona.getNumeroDocumento());

        if (existente.isPresent()) {
            log.info("Persona encontrada, reutilizando ID={}", existente.get().getIdPersona());
            return existente.get();
        }

        // 2. Validar reglas NATURAL / JURIDICA
        validarPersona(persona);

        // 3. Generar nombre completo
        persona.setNombreCompleto(generarNombreCompleto(persona));

        // 4. Auditoría
        persona.setIdUsuarioCreacion(idUsuario);
        persona.setFechaCreacion(LocalDateTime.now());
        persona.setEstado(1);

        // 5. Guardar
        Persona saved = repository.save(persona);

        // 🔥 LIMPIAR CONTEXTO
        entityManager.flush();
        entityManager.clear();

        log.info("Persona creada ID={}", saved.getIdPersona());

        // 🔥 Notificar a toda la aplicación
        publisher.publishEvent(new PersonaActualizadaEvent(this, saved.getIdPersona()));

        return saved;
    }

    // ============================================================
    // ACTUALIZAR PERSONA
    // ============================================================
    @Override
    @Transactional
    @Auditable(
            accion = AuditAction.ACTUALIZAR,
            entidad = "PERSONA"
    )
    public Persona actualizar(Persona persona, Integer idUsuario) {

        log.info("Actualizando persona ID={}", persona.getIdPersona());

        validarPersona(persona);
        persona.setNombreCompleto(generarNombreCompleto(persona));
        persona.setIdUsuarioModificacion(idUsuario);
        persona.setFechaActualizacion(LocalDateTime.now());

        Persona updated = repository.save(persona);

        entityManager.flush();
        entityManager.clear();

        publisher.publishEvent(new PersonaActualizadaEvent(this, persona.getIdPersona()));

        return updated;
    }

    // ============================================================
    // DESACTIVAR PERSONA
    // ============================================================
    @Override
    @Transactional
    @Auditable(
            accion = AuditAction.ELIMINAR,
            entidad = "PERSONA"
    )
    public void desactivar(Integer idPersona, Integer idUsuario) {

        log.info("Intentando desactivar persona ID={}", idPersona);

        repository.findById(idPersona).ifPresent(p -> {
            p.setEstado(0);
            p.setIdUsuarioModificacion(idUsuario);
            p.setFechaActualizacion(LocalDateTime.now());
            repository.save(p);

            publisher.publishEvent(new PersonaActualizadaEvent(this, idPersona));

            log.info("Persona desactivada ID={}", idPersona);
        });
    }

    // ============================================================
    // MÉTODOS AUXILIARES
    // ============================================================

    @Override
    public Optional<Persona> buscarPorId(Integer idPersona) {
        return repository.findById(idPersona);
    }

    @Override
    public boolean existeDocumento(String numeroDocumento) {
        return repository.existsByNumeroDocumento(numeroDocumento);
    }

    // ============================================================
    // VALIDACIONES
    // ============================================================

    private void validarPersona(Persona p) {

        if (p.getTipoPersona() == TipoPersona.NATURAL) {
            validarNatural(p);
        } else {
            validarJuridica(p);
        }
    }

    private void validarNatural(Persona p) {
        if (p.getNombres() == null || p.getApellidos() == null) {
            throw new IllegalStateException("Para persona NATURAL se requieren nombres y apellidos.");
        }
        if (p.getTipoDocumento() == TipoDocumento.RUC) {
            throw new IllegalStateException("Una persona NATURAL no puede tener RUC.");
        }
        p.setRazonSocial(null);
    }

    private void validarJuridica(Persona p) {
        if (p.getRazonSocial() == null) {
            throw new IllegalStateException("Para persona JURIDICA se requiere razón social.");
        }
        if (p.getTipoDocumento() != TipoDocumento.RUC) {
            throw new IllegalStateException("Una persona JURIDICA debe tener tipo_documento = RUC.");
        }
        p.setNombres(null);
        p.setApellidos(null);
    }

    private String generarNombreCompleto(Persona p) {
        return (p.getTipoPersona() == TipoPersona.NATURAL)
                ? p.getNombres() + " " + p.getApellidos()
                : p.getRazonSocial();
    }
}