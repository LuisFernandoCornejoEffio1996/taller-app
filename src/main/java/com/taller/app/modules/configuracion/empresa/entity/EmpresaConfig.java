package com.taller.app.modules.configuracion.empresa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Configuración de la empresa del sistema.
 */
@Getter
@Setter
@Entity
@Table(name = "empresa_config")
public class EmpresaConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa_config")
    private Integer idEmpresaConfig;

    private String ruc;

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "nombre_comercial")
    private String nombreComercial;

    private String direccion;

    private String telefono;

    private String email;

    @Column(name = "logo_path")
    private String logoPath;

    private Integer estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_modificacion")
    private Integer usuarioModificacion;

}
