package com.taller.app.modules.persona.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer idPersona;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_persona", nullable = false)
    private TipoPersona tipoPersona; // NATURAL / JURIDICA

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento; // DNI, CE, PASAPORTE, RUC

    @Column(name = "numero_documento", nullable = false, unique = true)
    private String numeroDocumento;

    // NATURAL
    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    // JURIDICA
    @Column(name = "razon_social")
    private String razonSocial;

    // Común
    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "email")
    private String email;

    @Column(name = "estado")
    private Integer estado;

    // Auditoría
    @Column(name = "id_usuario_creacion", nullable = false)
    private Integer idUsuarioCreacion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "id_usuario_modificacion")
    private Integer idUsuarioModificacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // ============================
    // LIFECYCLE HOOKS
    // ============================

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
        estado = 1;
    }

    @PreUpdate
    public void preUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}

