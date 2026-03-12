package com.taller.app.core.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Mantiene información del usuario en sesión.
 */
@Component
@Getter
@Setter
public class SessionContext {

    private Integer usuarioId;

    private String username;

}
