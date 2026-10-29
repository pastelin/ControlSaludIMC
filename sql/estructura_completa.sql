CREATE DATABASE IF NOT EXISTS control_salud_imc
 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE control_salud_imc;

CREATE TABLE IF NOT EXISTS usuarios (
 id BIGINT NOT NULL AUTO_INCREMENT,
 nombre_completo VARCHAR(120) NOT NULL,
 usuario VARCHAR(50) NOT NULL,
 contrasena_hash VARCHAR(100) NOT NULL,
 edad INTEGER NOT NULL,
 sexo VARCHAR(20) NOT NULL,
 estatura DOUBLE NOT NULL,
 peso_inicial DOUBLE NULL,
 fecha_registro DATETIME(6) NOT NULL,
 PRIMARY KEY(id),
 CONSTRAINT uk_usuario_nombre UNIQUE(usuario),
 CONSTRAINT ck_usuario_edad CHECK (edad BETWEEN 15 AND 120),
 CONSTRAINT ck_usuario_estatura CHECK (estatura BETWEEN 1 AND 2.5),
 CONSTRAINT ck_usuario_peso_inicial CHECK (peso_inicial IS NULL OR peso_inicial > 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS imc (
 id BIGINT NOT NULL AUTO_INCREMENT,
 id_usuario BIGINT NOT NULL,
 peso DOUBLE NOT NULL,
 estatura DOUBLE NOT NULL,
 valor_imc DOUBLE NOT NULL,
 estado VARCHAR(30) NOT NULL,
 fecha DATETIME(6) NOT NULL,
 PRIMARY KEY(id),
 INDEX ix_imc_usuario(id_usuario),
 CONSTRAINT fk_imc_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
 CONSTRAINT ck_imc_peso CHECK (peso > 0),
 CONSTRAINT ck_imc_estatura CHECK (estatura BETWEEN 1 AND 2.5)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS directories (
 id BIGINT NOT NULL AUTO_INCREMENT,
 id_usuario BIGINT NOT NULL,
 nombre VARCHAR(80) NOT NULL,
 PRIMARY KEY(id),
 INDEX ix_directory_usuario(id_usuario),
 CONSTRAINT fk_directory_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS files (
 id BIGINT NOT NULL AUTO_INCREMENT,
 id_usuario BIGINT NOT NULL,
 id_directorio BIGINT NOT NULL,
 nombre VARCHAR(100) NOT NULL,
 contenido LONGTEXT NOT NULL,
 fecha DATETIME(6) NOT NULL,
 PRIMARY KEY(id),
 INDEX ix_file_usuario(id_usuario),
 INDEX ix_file_directorio(id_directorio),
 CONSTRAINT fk_file_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
 CONSTRAINT fk_file_directorio FOREIGN KEY (id_directorio) REFERENCES directories(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS notifications (
 id BIGINT NOT NULL AUTO_INCREMENT,
 id_usuario BIGINT NOT NULL,
 mensaje VARCHAR(250) NOT NULL,
 leida BIT NOT NULL,
 fecha DATETIME(6) NOT NULL,
 PRIMARY KEY(id),
 INDEX ix_notify_usuario(id_usuario),
 CONSTRAINT fk_notify_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
) ENGINE=InnoDB;

SHOW TABLES;
