package com.taller.app.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VistaCambiadaEvent extends ApplicationEvent {

    private final String tituloModulo;

    public VistaCambiadaEvent(Object source, String tituloModulo) {
        super(source);
        this.tituloModulo = tituloModulo;
    }
}

