package com.taller.app.events;

import org.springframework.context.ApplicationEvent;

/**
 * Evento que se dispara cuando una persona es creada, actualizada o desactivada.
 *
 * Permite que otros módulos (empleado, cliente, proveedor)
 * refresquen sus vistas o sincronicen datos en tiempo real.
 */
public class PersonaActualizadaEvent extends ApplicationEvent {

    private final Integer idPersona;

    public PersonaActualizadaEvent(Object source, Integer idPersona) {
        super(source);
        this.idPersona = idPersona;
    }

    public Integer getIdPersona() {
        return idPersona;
    }
}

