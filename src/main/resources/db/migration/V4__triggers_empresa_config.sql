*-- Trigger BEFORE INSERT --*
DELIMITER $$

CREATE TRIGGER trg_empresa_config_insert
BEFORE INSERT ON empresa_config
FOR EACH ROW
BEGIN
    DECLARE v_id_auditoria INT;

    -- Insertamos en auditoria (maestro)
    INSERT INTO auditoria (id_usuario, entidad, id_entidad, accion, detalle)
    VALUES (NEW.usuario_modificacion, 'empresa_config', NEW.id_empresa_config, 'INSERT', 'Creación de registro');

    SET v_id_auditoria = LAST_INSERT_ID();

    -- Insertamos detalle por cada campo
    INSERT INTO auditoria_detalle (id_auditoria, entidad, id_entidad, campo, valor_anterior, valor_nuevo, id_usuario)
    VALUES
        (v_id_auditoria, 'empresa_config', NEW.id_empresa_config, 'ruc', NULL, NEW.ruc, NEW.usuario_modificacion),
        (v_id_auditoria, 'empresa_config', NEW.id_empresa_config, 'razon_social', NULL, NEW.razon_social, NEW.usuario_modificacion),
        (v_id_auditoria, 'empresa_config', NEW.id_empresa_config, 'nombre_comercial', NULL, NEW.nombre_comercial, NEW.usuario_modificacion),
        (v_id_auditoria, 'empresa_config', NEW.id_empresa_config, 'direccion', NULL, NEW.direccion, NEW.usuario_modificacion),
        (v_id_auditoria, 'empresa_config', NEW.id_empresa_config, 'telefono', NULL, NEW.telefono, NEW.usuario_modificacion),
        (v_id_auditoria, 'empresa_config', NEW.id_empresa_config, 'email', NULL, NEW.email, NEW.usuario_modificacion),
        (v_id_auditoria, 'empresa_config', NEW.id_empresa_config, 'logo_path', NULL, NEW.logo_path, NEW.usuario_modificacion),
        (v_id_auditoria, 'empresa_config', NEW.id_empresa_config, 'estado', NULL, NEW.estado, NEW.usuario_modificacion);
END$$

DELIMITER ;

*-- Trigger BEFORE UPDATE --*
DELIMITER $$

CREATE TRIGGER trg_empresa_config_update
BEFORE UPDATE ON empresa_config
FOR EACH ROW
BEGIN
    DECLARE v_id_auditoria INT;

    INSERT INTO auditoria (id_usuario, entidad, id_entidad, accion, detalle)
    VALUES (NEW.usuario_modificacion, 'empresa_config', OLD.id_empresa_config, 'UPDATE', 'Actualización de registro');

    SET v_id_auditoria = LAST_INSERT_ID();

    -- Campo por campo
    IF OLD.ruc <> NEW.ruc THEN
        INSERT INTO auditoria_detalle VALUES (NULL, v_id_auditoria, 'empresa_config', OLD.id_empresa_config, 'ruc', OLD.ruc, NEW.ruc, NEW.usuario_modificacion, NOW());
    END IF;

    IF OLD.razon_social <> NEW.razon_social THEN
        INSERT INTO auditoria_detalle VALUES (NULL, v_id_auditoria, 'empresa_config', OLD.id_empresa_config, 'razon_social', OLD.razon_social, NEW.razon_social, NEW.usuario_modificacion, NOW());
    END IF;

    IF OLD.nombre_comercial <> NEW.nombre_comercial THEN
        INSERT INTO auditoria_detalle VALUES (NULL, v_id_auditoria, 'empresa_config', OLD.id_empresa_config, 'nombre_comercial', OLD.nombre_comercial, NEW.nombre_comercial, NEW.usuario_modificacion, NOW());
    END IF;

    IF OLD.direccion <> NEW.direccion THEN
        INSERT INTO auditoria_detalle VALUES (NULL, v_id_auditoria, 'empresa_config', OLD.id_empresa_config, 'direccion', OLD.direccion, NEW.direccion, NEW.usuario_modificacion, NOW());
    END IF;

    IF OLD.telefono <> NEW.telefono THEN
        INSERT INTO auditoria_detalle VALUES (NULL, v_id_auditoria, 'empresa_config', OLD.id_empresa_config, 'telefono', OLD.telefono, NEW.telefono, NEW.usuario_modificacion, NOW());
    END IF;

    IF OLD.email <> NEW.email THEN
        INSERT INTO auditoria_detalle VALUES (NULL, v_id_auditoria, 'empresa_config', OLD.id_empresa_config, 'email', OLD.email, NEW.email, NEW.usuario_modificacion, NOW());
    END IF;

    IF OLD.logo_path <> NEW.logo_path THEN
        INSERT INTO auditoria_detalle VALUES (NULL, v_id_auditoria, 'empresa_config', OLD.id_empresa_config, 'logo_path', OLD.logo_path, NEW.logo_path, NEW.usuario_modificacion, NOW());
    END IF;

    IF OLD.estado <> NEW.estado THEN
        INSERT INTO auditoria_detalle VALUES (NULL, v_id_auditoria, 'empresa_config', OLD.id_empresa_config, 'estado', OLD.estado, NEW.estado, NEW.usuario_modificacion, NOW());
    END IF;

END$$

DELIMITER ;


*-- Trigger BEFORE DELETE --*
DELIMITER $$

CREATE TRIGGER trg_empresa_config_delete
BEFORE DELETE ON empresa_config
FOR EACH ROW
BEGIN
    DECLARE v_id_auditoria INT;

    INSERT INTO auditoria (id_usuario, entidad, id_entidad, accion, detalle)
    VALUES (OLD.usuario_modificacion, 'empresa_config', OLD.id_empresa_config, 'DELETE', 'Eliminación de registro');

    SET v_id_auditoria = LAST_INSERT_ID();

    INSERT INTO auditoria_detalle (id_auditoria, entidad, id_entidad, campo, valor_anterior, valor_nuevo, id_usuario)
    VALUES (v_id_auditoria, 'empresa_config', OLD.id_empresa_config, 'registro_completo',
            CONCAT('RUC=', OLD.ruc, ', RS=', OLD.razon_social),
            NULL,
            OLD.usuario_modificacion);
END$$

DELIMITER ;
