package com.taller.app.modules.shared.auditoria.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.*;
@Slf4j
@Repository
@RequiredArgsConstructor
public class AuditoriaDetalleDAO {
    private final JdbcTemplate jdbcTemplate;

    public void registrarCambio(String entidad,
                                Integer idEntidad,
                                Integer idUsuario,
                                String campo,
                                String valorAnterior,
                                String valorNuevo) {

        String sql = """
            INSERT INTO auditoria_detalle
            (entidad, id_entidad, campo, valor_anterior, valor_nuevo, id_usuario, fecha)
            VALUES (?, ?, ?, ?, ?, ?, NOW())
        """;

        jdbcTemplate.update(sql,
                entidad,
                idEntidad,
                campo,
                valorAnterior,
                valorNuevo,
                idUsuario
        );

        log.debug("Auditoría registrada: {}.{} campo={} usuario={}", entidad, idEntidad, campo, idUsuario);

    }
}

