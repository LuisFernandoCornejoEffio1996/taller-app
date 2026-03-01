package com.taller.app.modules.shared.auditoria.repository;

import com.taller.app.modules.shared.auditoria.model.AuditoriaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaDetalleRepository extends JpaRepository<AuditoriaDetalle, Integer> {

    List<AuditoriaDetalle> findByEntidadOrderByFechaDesc(String entidad);

    List<AuditoriaDetalle> findByEntidadAndIdEntidadOrderByFechaDesc(String entidad, Integer idEntidad);

    List<AuditoriaDetalle> findByIdUsuarioOrderByFechaDesc(Integer idUsuario);

    List<AuditoriaDetalle> findByFechaBetweenOrderByFechaDesc(LocalDateTime inicio, LocalDateTime fin);

    List<AuditoriaDetalle> findByEntidadAndCampoOrderByFechaDesc(String entidad, String campo);
}
