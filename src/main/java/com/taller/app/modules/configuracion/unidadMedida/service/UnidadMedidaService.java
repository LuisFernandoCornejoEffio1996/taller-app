package com.taller.app.modules.configuracion.unidadMedida.service;

import com.taller.app.modules.configuracion.unidadMedida.entity.UnidadMedida;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de unidad de medida
 */
public interface UnidadMedidaService {

    List<UnidadMedida> listar();

    List<UnidadMedida> listarActivos();

    Optional<UnidadMedida> buscarPorId(Integer id);

    UnidadMedida crearOActualizar(UnidadMedida unidad);

    void desactivar(Integer id);

    boolean existeNombre(String nombre);

    boolean existeAbreviatura(String abreviatura);

}
