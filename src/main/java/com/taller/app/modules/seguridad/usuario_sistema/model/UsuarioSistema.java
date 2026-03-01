package com.taller.app.modules.seguridad.usuario_sistema.model;

import com.taller.app.modules.seguridad.persona.model.Persona;
import com.taller.app.util.enums.EstadoRegistro;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario_sistema")
public class UsuarioSistema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "primer_acceso")
    private Boolean primerAcceso;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "estado")
    private EstadoRegistro estado;
}

