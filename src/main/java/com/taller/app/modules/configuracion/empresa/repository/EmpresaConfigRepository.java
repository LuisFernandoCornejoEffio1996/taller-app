package com.taller.app.modules.configuracion.empresa.repository;

import com.taller.app.modules.configuracion.empresa.model.EmpresaConfig;
import com.taller.app.util.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpresaConfigRepository extends JpaRepository<EmpresaConfig, Integer> {

    Optional<EmpresaConfig> findFirstByEstado(EstadoRegistro estado);

    Optional<EmpresaConfig> findByRuc(String ruc);

    boolean existsByEstado(EstadoRegistro estado);
}