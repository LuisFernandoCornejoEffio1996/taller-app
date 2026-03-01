package com.taller.app.modules.configuracion.empresa.model;

import com.taller.app.modules.seguridad.usuario_sistema.model.UsuarioSistema;
import com.taller.app.util.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "empresa_config")
public class EmpresaConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa_config")
    private Integer idEmpresaConfig;

    @Column(name = "ruc", length = 11, nullable = false, unique = true)
    private String ruc;

    @Column(name = "razon_social", length = 150, nullable = false)
    private String razonSocial;

    @Column(name = "nombre_comercial", length = 150)
    private String nombreComercial;

    @Column(name = "direccion", length = 200)
    private String direccion;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "logo_path", length = 300)
    private String logoPath;

    @Enumerated(EnumType.ORDINAL) // si usas tinyint 0/1
    private EstadoRegistro estado;

    // La BD maneja estos valores automáticamente
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_modificacion")
    private UsuarioSistema usuarioModificacion;

}

