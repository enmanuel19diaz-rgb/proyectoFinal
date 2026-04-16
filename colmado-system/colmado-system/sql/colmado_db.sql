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

-- Esta sera la configuracion del negocio

CREATE TABLE configuracion (
                               id INT PRIMARY KEY DEFAULT 1,
                               nombre_negocio VARCHAR(100) NOT NULL,
                               rnc VARCHAR(20),
                               direccion VARCHAR(200),
                               telefono VARCHAR(15)
);

-- Insertamos los datos por defecto la primera vez
INSERT INTO configuracion (id, nombre_negocio, rnc, direccion, telefono)
VALUES (1, 'Colmado la Venganza', '123-4567890-1', 'La caleta, AV panamericana', '809-123-4567');

--Productos
INSERT INTO productos (nombre, precio, cantidad, descripcion, categoria) VALUES
-- GRANOS Y ABARROTES
('Arroz Selecto Campo (lb)', 35.00, 500, 'Arroz blanco de grano largo', 'Granos'),
('Habichuelas Rojas (lb)', 75.00, 150, 'Habichuelas secas de primera', 'Granos'),
('Habichuelas Negras (lb)', 70.00, 100, 'Habichuelas para moro', 'Granos'),
('Azúcar Crema (lb)', 32.00, 200, 'Azúcar refinada', 'Dulces'),
('Sal de Mar (lb)', 15.00, 100, 'Sal fina de mesa', 'Condimentos'),

-- LÁCTEOS Y HUEVOS
('Leche Rica Listamilk 1L', 78.00, 48, 'Leche entera pasteurizada', 'Lácteos'),
('Leche Milex Kinder 1600g', 1450.00, 12, 'Leche en polvo', 'Lácteos'),
('Yogurt Rica Fresa 1L', 125.00, 20, 'Yogurt líquido con frutas', 'Lácteos'),
('Huevo de Granja (unidad)', 7.00, 360, 'Huevo fresco de gallina', 'Proteínas'),
('Queso Geo (lb)', 380.00, 15, 'Queso tipo holandés', 'Embutidos'),

-- EMBUTIDOS Y CARNES
('Salami Induveca Super Especial', 225.00, 30, 'Salami tradicional', 'Embutidos'),
('Salami Checo 2lb', 185.00, 15, 'Salami de primera', 'Embutidos'),
('Jamón Caserío (lb)', 210.00, 10, 'Jamón de pavo cocido', 'Embutidos'),

-- ACEITES Y CONDIMENTOS
('Aceite Crisol 128oz', 550.00, 24, 'Aceite de soya vegetal', 'Aceites'),
('Aceite La Joya 1L', 190.00, 40, 'Aceite para cocinar', 'Aceites'),
('Sopita Maggi (unidad)', 5.00, 500, 'Caldito de pollo en cubo', 'Condimentos'),
('Pasta de Tomate Linda 400g', 65.00, 35, 'Concentrado de tomate', 'Enlatados'),

-- BEBIDAS
('Refresco Coca Cola 2L', 95.00, 36, 'Refresco de cola familiar', 'Bebidas'),
('Refresco Country Club Frambuesa', 35.00, 60, 'Refresco de 20oz', 'Bebidas'),
('Jugo Santal Naranja 1L', 110.00, 24, 'Jugo de fruta natural', 'Bebidas'),
('Malta Morena 12oz', 45.00, 72, 'Bebida refrescante de malta', 'Bebidas'),
('Cerveza Presidente 650ml', 165.00, 48, 'Cerveza pilsener nacional', 'Bebidas'),

-- CAFÉ Y DESAYUNO
('Café Santo Domingo 12oz', 255.00, 25, 'Café molido dominicano', 'Café'),
('Chocolate Embajador (unidad)', 10.00, 100, 'Tableta de chocolate amargo', 'Desayuno'),
('Corn Flakes Kellogg 500g', 215.00, 15, 'Cereal de maíz tostado', 'Desayuno'),
('Galletas Guarina Leche', 15.00, 120, 'Galletas de soda tradicionales', 'Dulces'),

-- LIMPIEZA
('Jabón de Cuaba Hispano', 38.00, 80, 'Jabón para lavar y fregar', 'Limpieza'),
('Cloro Macier 1L', 45.00, 50, 'Desinfectante líquido', 'Limpieza'),
('Detergente Ace 1lb', 85.00, 40, 'Detergente en polvo', 'Limpieza');