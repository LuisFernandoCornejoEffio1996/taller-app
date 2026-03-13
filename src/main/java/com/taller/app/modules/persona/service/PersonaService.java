package com.taller.app.modules.persona.service;

import com.taller.app.modules.persona.entity.Persona;

import java.util.Optional;

/**
 * Servicio de gestión de personas.
 *
 * Responsable de:
 *  - Crear o reutilizar personas según documento
 *  - Validar reglas NATURAL / JURIDICA
 *  - Generar nombre_completo
 *  - Registrar auditoría
 *  - Desactivar persona (solo si no tiene roles activos)
 */
public interface PersonaService {

    /**
     * Obtiene una persona existente por documento o crea una nueva.
     */
    Persona obtenerOCrear(Persona persona, Integer idUsuario);

    /**
     * Actualiza los datos de una persona.
     */
    Persona actualizar(Persona persona, Integer idUsuario);

    /**
     * Busca persona por ID.
     */
    Optional<Persona> buscarPorId(Integer idPersona);

    /**
     * Desactiva una persona (solo si no tiene roles activos).
     */
    void desactivar(Integer idPersona, Integer idUsuario);

    /**
     * Verifica si existe una persona con el documento indicado.
     */
    boolean existeDocumento(String numeroDocumento);
}

