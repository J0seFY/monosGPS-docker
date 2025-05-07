CREATE TABLE establecimiento (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    telefono VARCHAR(50)
);

CREATE TABLE estudiante (
    rut VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(50),
    curso VARCHAR(100),
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
