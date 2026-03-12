package com.taller.app.core.audit.repository;

import com.taller.app.core.audit.entity.Auditoria;
import com.taller.app.core.audit.entity.AuditoriaCambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para registrar cambios por cambios
 */
@Repository
public interface AuditoriaCambioRepository extends JpaRepository<AuditoriaCambio, Integer> {
}
