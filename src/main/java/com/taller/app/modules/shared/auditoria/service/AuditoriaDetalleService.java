package com.taller.app.modules.shared.auditoria.service;

import com.taller.app.modules.shared.auditoria.model.AuditoriaDetalle;
import com.taller.app.modules.shared.auditoria.repository.AuditoriaDetalleRepository;
import lombok.*;
import org.springframework.stereotype.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaDetalleService {

    private final AuditoriaDetalleRepository repository;

    public List<AuditoriaDetalle> listarPorEntidad(String entidad) {
        return repository.findByEntidadOrderByFechaDesc(entidad);
    }

    public List<AuditoriaDetalle> listarPorEntidadYRegistro(String entidad, Integer idEntidad) {
        return repository.findByEntidadAndIdEntidadOrderByFechaDesc(entidad, idEntidad);
    }

    public List<AuditoriaDetalle> listarPorUsuario(Integer idUsuario) {
        return repository.findByIdUsuarioOrderByFechaDesc(idUsuario);
    }

    public List<AuditoriaDetalle> listarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return repository.findByFechaBetweenOrderByFechaDesc(inicio, fin);
    }

    public List<AuditoriaDetalle> listarPorCampo(String entidad, String campo) {
        return repository.findByEntidadAndCampoOrderByFechaDesc(entidad, campo);
    }
}

