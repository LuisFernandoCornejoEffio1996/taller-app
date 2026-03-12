CREATE TABLE auditoria_cambio (
    id_auditoria_cambio INT NOT NULL AUTO_INCREMENT,

    id_auditoria INT NOT NULL,

    campo VARCHAR(100) NOT NULL,
    valor_anterior TEXT NULL,
    valor_nuevo TEXT NULL,

    PRIMARY KEY (id_auditoria_cambio),

    INDEX idx_aud_cambio_auditoria (id_auditoria),

    CONSTRAINT fk_cambio_auditoria
        FOREIGN KEY (id_auditoria)
        REFERENCES auditoria(id_auditoria)
        ON DELETE CASCADE
);
