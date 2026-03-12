package com.taller.app.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmpresaActualizadaEvent extends ApplicationEvent {

    private final String nuevoNombre;

    public EmpresaActualizadaEvent(Object source, String nuevoNombre) {
        super(source);
        this.nuevoNombre = nuevoNombre;
    }
}

