package com.taller.app.modules.configuracion.empresa.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EmpresaConfigAuditoriaDAO {

    private final JdbcTemplate jdbcTemplate;

    public void registrarCambio(Integer idEmpresaConfig,
                                Integer idUsuario,
                                String campo,
                                String valorAnterior,
                                String valorNuevo) {

        String sql = """
            INSERT INTO auditoria_empresa_config
            (id_empresa_config, id_usuario, campo, valor_anterior, valor_nuevo, fecha)
            VALUES (?, ?, ?, ?, ?, NOW())
        """;

        jdbcTemplate.update(sql,
                idEmpresaConfig,
                idUsuario,
                campo,
                valorAnterior,
                valorNuevo
        );
    }
}

