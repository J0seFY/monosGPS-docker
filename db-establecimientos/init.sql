CREATE TABLE establecimiento (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    comuna VARCHAR(100) NOT NULL,
    telefono VARCHAR(50)
);

CREATE TABLE estudiante (
    rut VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(50),
    curso VARCHAR(100),
    nacionalidad VARCHAR(100),
    estado VARCHAR(50),
    establecimiento_id INT REFERENCES establecimiento(id) ON DELETE CASCADE
);

CREATE TABLE profesor (
    rut VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    telefono VARCHAR(50),
    asignatura VARCHAR(100),
    establecimiento_id INT REFERENCES establecimiento(id) ON DELETE CASCADE
);

-- Tabla para apoderados
CREATE TABLE apoderado (
    rut VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    telefono VARCHAR(50)
);

-- Relación entre apoderado y estudiante
CREATE TABLE estudiante_apoderado (
    estudiante_rut VARCHAR(20) REFERENCES estudiante(rut) ON DELETE CASCADE,
    apoderado_rut VARCHAR(20) REFERENCES apoderado(rut) ON DELETE CASCADE,
    PRIMARY KEY (estudiante_rut, apoderado_rut)
);

-- Tabla para accidentes escolares
CREATE TABLE accidente_escolar (
    id SERIAL PRIMARY KEY,
    estudiante_rut VARCHAR(20) REFERENCES estudiante(rut),
    fecha DATE NOT NULL,
    descripcion TEXT,
    establecimiento_id INT REFERENCES establecimiento(id)
);

-- Tabla para asistencia
CREATE TABLE asistencia (
    id SERIAL PRIMARY KEY,
    estudiante_rut VARCHAR(20) REFERENCES estudiante(rut),
    fecha DATE NOT NULL,
    presente BOOLEAN NOT NULL
);

-- Tabla para rendimiento (por asignatura)
CREATE TABLE rendimiento (
    id SERIAL PRIMARY KEY,
    estudiante_rut VARCHAR(20) REFERENCES estudiante(rut),
    asignatura VARCHAR(100),
    nota DECIMAL(3,1),
    fecha DATE
);

-- Tabla para mensajes
CREATE TABLE mensaje (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255),
    contenido TEXT,
    fecha_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_destinatario VARCHAR(100) -- ej: "apoderados", "profesores", "todos"
);

-- Relación mensajes <-> establecimientos
CREATE TABLE mensaje_establecimiento (
    mensaje_id INT REFERENCES mensaje(id),
    establecimiento_id INT REFERENCES establecimiento(id),
    PRIMARY KEY (mensaje_id, establecimiento_id)
);

-- Tabla para pruebas como SIMCE, PAES
CREATE TABLE resultado_prueba (
    id SERIAL PRIMARY KEY,
    estudiante_rut VARCHAR(20) REFERENCES estudiante(rut),
    tipo_prueba VARCHAR(50), -- ej: "SIMCE", "PAES"
    puntaje INT,
    fecha DATE
);
