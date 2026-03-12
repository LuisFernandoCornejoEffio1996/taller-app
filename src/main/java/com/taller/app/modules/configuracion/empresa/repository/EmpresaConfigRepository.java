package com.taller.app.modules.configuracion.empresa.repository;

import com.taller.app.modules.configuracion.empresa.entity.EmpresaConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Acceso a datos de empresa
 */
@Repository
public interface EmpresaConfigRepository extends JpaRepository<EmpresaConfig, Integer> {
    @Query("SELECT e FROM EmpresaConfig e WHERE e.estado = 1")
    Optional<EmpresaConfig> obtenerEmpresaActiva();
}
