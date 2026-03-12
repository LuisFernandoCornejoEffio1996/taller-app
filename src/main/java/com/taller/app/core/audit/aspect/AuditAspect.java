package com.taller.app.core.audit.aspect;

import com.taller.app.core.audit.AuditAction;
import com.taller.app.core.audit.Auditable;
import com.taller.app.core.audit.entity.Auditoria;
import com.taller.app.core.audit.entity.AuditoriaCambio;
import com.taller.app.core.audit.repository.AuditoriaRepository;
import com.taller.app.core.audit.util.ReflectionUtils;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Aspecto encargado de registrar auditoría automáticamente.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditoriaRepository auditoriaRepository;
    private final EntityManager entityManager;

    @Around("@annotation(auditable)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {

        log.info("=== AUDITORIA INICIADA ===");

        Object arg = joinPoint.getArgs()[0];
        Object oldObject = null;

        // 🔥 Solo cargar oldObject si es ACTUALIZAR
        if (auditable.accion() == AuditAction.ACTUALIZAR) {

            Integer id = ReflectionUtils.getEntityId(arg);

            if (id != null) {
                Object original = entityManager.find(arg.getClass(), id);

                if (original != null) {
                    oldObject = ReflectionUtils.deepCopyEntity(original);
                }

                log.info("Entidad original BD: {}", oldObject);
            }
        }

        // 🔥 Ejecutar método (Hibernate genera ID aquí)
        Object result = joinPoint.proceed();

        Integer idEntidad = ReflectionUtils.getEntityId(result);

        if (idEntidad == null) {
            log.error("No se pudo obtener ID de la entidad para auditoría");
            return result;
        }

        // 🔥 Registrar auditoría principal
        Auditoria auditoria = new Auditoria();
        auditoria.setFecha(LocalDateTime.now());
        auditoria.setEntidad(auditable.entidad());
        auditoria.setAccion(auditable.accion().name());
        auditoria.setIdEntidad(idEntidad);
        auditoria.setIdUsuario(1);

        auditoria = auditoriaRepository.save(auditoria);

        log.info("Auditoria registrada ID {}", auditoria.getIdAuditoria());

        // 🔥 Registrar cambios si aplica
        if (oldObject != null) {

            Map<String, String[]> changes =
                    ReflectionUtils.detectChanges(oldObject, result);

            log.info("Cambios detectados: {}", changes.size());

            List<AuditoriaCambio> lista = new ArrayList<>();

            for (Map.Entry<String, String[]> entry : changes.entrySet()) {

                AuditoriaCambio cambio = new AuditoriaCambio();
                cambio.setAuditoria(auditoria);
                cambio.setCampo(entry.getKey());
                cambio.setValorAnterior(entry.getValue()[0]);
                cambio.setValorNuevo(entry.getValue()[1]);

                lista.add(cambio);
            }

            auditoria.setCambios(lista);
            auditoriaRepository.save(auditoria);

            log.info("Cambios persistidos {}", lista.size());
        }

        log.info("=== AUDITORIA FINALIZADA ===");

        return result;
    }
}

