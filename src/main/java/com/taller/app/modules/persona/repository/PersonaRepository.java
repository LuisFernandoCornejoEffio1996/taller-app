package com.taller.app.modules.persona.repository;

import com.taller.app.modules.persona.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Acceso a datos para la entidad Persona.
 *
 * Este repositorio se encarga únicamente de:
 *  - Búsqueda por documento
 *  - Verificar duplicados
 *  - Listar activas/inactivas
 *  - Acceso CRUD básico
 *
 */
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

    /**
     * Busca una persona por su número de documento.
     * El documento es único en toda la tabla.
     */
    Optional<Persona> findByNumeroDocumento(String numeroDocumento);

    /**
     * Verifica si ya existe una persona con el documento indicado.
     * Útil para validaciones previas.
     */
    boolean existsByNumeroDocumento(String numeroDocumento);

    /**
     * Lista todas las personas activas (estado = 1).
     */
    List<Persona> findByEstado(Integer estado);

    /**
     * Lista personas activas filtrando por nombre completo.
     * Útil para búsquedas en módulos que consumen Persona.
     */
    List<Persona> findByEstadoAndNombreCompletoContainingIgnoreCase(Integer estado, String nombreCompleto);
}

