q-- colmado_db, debes ejecutar este script en su MySQL Workbench
-- o desde la terminal para crear la base de datos en su computadora.

CREATE DATABASE IF NOT EXISTS colmado_db;
       USE colmado_db;
CREATE TABLE usuarios ( id_usuario INT PRIMARY KEY AUTO_INCREMENT,
                        username VARCHAR(50) NOT NULL UNIQUE,
                        password VARCHAR(100) NOT NULL,
                        rol ENUM('ADMIN','CAJERO') NOT NULL );

CREATE TABLE productos ( id_producto INT PRIMARY KEY AUTO_INCREMENT,
                         nombre VARCHAR(100) NOT NULL,
                         precio DOUBLE NOT NULL,
                         cantidad INT NOT NULL,
                         descripcion VARCHAR(255),
                         categoria VARCHAR(50) );

CREATE TABLE clientes ( id_cliente INT PRIMARY KEY AUTO_INCREMENT,
                        nombre VARCHAR(100) NOT NULL,
                        telefono VARCHAR(15),
                        direccion VARCHAR(200),
                        email VARCHAR(100) );

CREATE TABLE ventas ( id_venta INT PRIMARY KEY AUTO_INCREMENT,
                      id_cliente INT,
                      fecha DATETIME DEFAULT NOW(),
                      total DOUBLE NOT NULL,
                      FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) );

CREATE TABLE detalle_venta ( id_detalle INT PRIMARY KEY AUTO_INCREMENT,
                             id_venta INT NOT NULL,
                             id_producto INT NOT NULL,
                             cantidad INT NOT NULL,
                             precio_unit DOUBLE NOT NULL,
                             FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
                             FOREIGN KEY (id_producto) REFERENCES productos(id_producto) );

-- Usuario de prueba: admin / 1234
INSERT INTO usuarios (username, password, rol) VALUES ('admin', '1234', 'ADMIN');