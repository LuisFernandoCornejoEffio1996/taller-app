package com.taller.app.modules.shared.auditoria.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "auditoria_detalle")
public class AuditoriaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    private String entidad;

    @Column(name = "id_entidad")
    private Integer idEntidad;

    private String campo;

    @Column(name = "valor_anterior")
    private String valorAnterior;

    @Column(name = "valor_nuevo")
    private String valorNuevo;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    private LocalDateTime fecha;
}
