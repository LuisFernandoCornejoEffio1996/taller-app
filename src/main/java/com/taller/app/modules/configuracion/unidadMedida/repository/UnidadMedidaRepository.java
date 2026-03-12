package com.taller.app.modules.configuracion.unidadMedida.repository;

import com.taller.app.modules.configuracion.unidadMedida.entity.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos de unidad de medida
 */
@Repository
public interface UnidadMedidaRepository extends JpaRepository<UnidadMedida, Integer> {

    // Buscar por nombre exacto
    Optional<UnidadMedida> findByNombreIgnoreCase(String nombre);

    // Buscar por abreviatura exacta
    Optional<UnidadMedida> findByAbreviaturaIgnoreCase(String abreviatura);

    // Validar duplicados por nombre
    boolean existsByNombreIgnoreCase(String nombre);

    // Validar duplicados por abreviatura
    boolean existsByAbreviaturaIgnoreCase(String abreviatura);

    // Listar solo activos (estado = 1)
    List<UnidadMedida> findByEstado(Integer estado);

    // Buscar activos por nombre
    List<UnidadMedida> findByEstadoAndNombreContainingIgnoreCase(Integer estado, String nombre);
}

