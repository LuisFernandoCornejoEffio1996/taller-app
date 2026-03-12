CREATE TABLE auditoria (
    id_auditoria INT NOT NULL AUTO_INCREMENT,

    id_usuario INT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    entidad VARCHAR(100) NOT NULL,
    id_entidad INT NOT NULL,

    accion VARCHAR(50) NOT NULL,

    PRIMARY KEY (id_auditoria),

    INDEX idx_auditoria_entidad (entidad, id_entidad),
    INDEX idx_auditoria_fecha (fecha),

    CONSTRAINT fk_auditoria_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario_sistema(id_usuario)
);


