package com.taller.app.modules.seguridad.usuario_sistema.service;

import com.taller.app.modules.seguridad.usuario_sistema.model.UsuarioSistema;
import com.taller.app.modules.seguridad.usuario_sistema.repository.UsuarioSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioSesionService {

    private final UsuarioSistemaRepository usuarioRepo;

    private UsuarioSistema usuarioActual;

    public UsuarioSistema getUsuarioActual() {

        // Si ya está cargado, lo devolvemos
        if (usuarioActual != null) {
            return usuarioActual;
        }

        // Cargar usuario temporal solo cuando se necesite
        usuarioActual = usuarioRepo.findById(1)
                .orElseThrow(() -> new IllegalStateException("No existe el usuario temporal con ID 1."));

        return usuarioActual;
    }
}


