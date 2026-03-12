package com.taller.app.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UnidadMedidaActualizadaEvent extends ApplicationEvent {

    private final Integer idUnidad;

    public UnidadMedidaActualizadaEvent(Object source, Integer idUnidad) {
        super(source);
        this.idUnidad = idUnidad;
    }
}

