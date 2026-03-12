package com.taller.app.core.audit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un evento de auditoría del sistema.
 * Se registra cuando ocurre una acción sobre una entidad (crear, actualizar, eliminar).
 */
@Getter
@Setter
@Entity
@Table(name = "auditoria")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    /**
     * Usuario que ejecuta la acción.
     * Si aún no existe login se puede enviar null o un valor fijo.
     */
    @Column(name = "id_usuario")
    private Integer idUsuario;

    /**
     * Fecha del evento.
     */
    @Column(name = "fecha")
    private LocalDateTime fecha;

    /**
     * Nombre de la entidad afectada (CLIENTE, PRODUCTO, etc).
     */
    @Column(name = "entidad")
    private String entidad;

    /**
     * Identificador de la entidad afectada.
     */
    @Column(name = "id_entidad")
    private Integer idEntidad;

    /**
     * Acción ejecutada (CREAR, ACTUALIZAR, ELIMINAR).
     */
    @Column(name = "accion")
    private String accion;

    /**
     * Cambios realizados sobre los campos.
     */
    @OneToMany(mappedBy = "auditoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuditoriaCambio> cambios = new ArrayList<>();

}

