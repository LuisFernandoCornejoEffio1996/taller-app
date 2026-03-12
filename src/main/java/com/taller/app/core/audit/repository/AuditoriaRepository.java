package com.taller.app.core.audit.repository;

import com.taller.app.core.audit.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para registrar eventos de auditoria
 */
@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {
}
