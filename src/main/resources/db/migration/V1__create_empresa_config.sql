CREATE TABLE IF NOT EXISTS empresa_config (
    id_empresa_config INT PRIMARY KEY AUTO_INCREMENT,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    razon_social VARCHAR(150) NOT NULL,
    nombre_comercial VARCHAR(150) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(50) NOT NULL,
    email VARCHAR(120) NOT NULL,
    logo_path VARCHAR(300) NOT NULL,
    estado TINYINT NOT NULL DEFAULT 1,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    usuario_modificacion INT NULL
);

-- Insertar registro inicial (empresa vacía)
INSERT INTO empresa_config (
    ruc,
    razon_social,
    nombre_comercial,
    direccion,
    telefono,
    email,
    logo_path,
    estado,
    usuario_modificacion
) VALUES (
    '00000000000',
    'EMPRESA SIN CONFIGURAR',
    'EMPRESA SIN CONFIGURAR',
    'SIN DIRECCIÓN',
    '000000000',
    'sin-email@local',
    '',
    1,
    NULL
);

