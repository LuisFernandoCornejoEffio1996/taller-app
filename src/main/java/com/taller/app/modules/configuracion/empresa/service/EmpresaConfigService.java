package com.taller.app.modules.configuracion.empresa.service;

import com.taller.app.modules.configuracion.empresa.entity.EmpresaConfig;

/**
 * Servicio de configuracion de la empresa
 */
public interface EmpresaConfigService {
    EmpresaConfig obtenerEmpresa();

    EmpresaConfig guardar(EmpresaConfig empresa);

}
