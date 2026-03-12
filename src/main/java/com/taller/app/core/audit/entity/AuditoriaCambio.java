package com.taller.app.core.audit.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa el cambio de un campo específico de una entidad.
 */
@Getter
@Setter
@Entity
@Table(name = "auditoria_cambio")
public class AuditoriaCambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria_cambio")
    private Integer idAuditoriaCambio;

    /**
     * Relación con el evento de auditoría.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_auditoria")
    private Auditoria auditoria;

    /**
     * Campo que fue modificado.
     */
    @Column(name = "campo")
    private String campo;

    /**
     * Valor antes del cambio.
     */
    @Column(name = "valor_anterior")
    private String valorAnterior;

    /**
     * Valor después del cambio.
     */
    @Column(name = "valor_nuevo")
    private String valorNuevo;

}
