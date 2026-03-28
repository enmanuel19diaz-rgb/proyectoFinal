# proyectoFinal
🛒 Sistema de Gestión para Colmado
Sistema de ventas e inventario desarrollado en Java con interfaz gráfica Swing y base de datos MySQL, construido con Maven como gestor de dependencias.

👥 Equipo de desarrollo
PersonaMóduloRamaPersona 1Base + Login + UI Principalfeature/base-loginPersona 2Productos + Inventariofeature/modulo-productosPersona 3Ventas + Facturafeature/modulo-ventasPersona 4Clientes + Reportesfeature/modulo-clientes-reportes

🧠 Tecnologías utilizadas

Java 17
Swing — interfaz gráfica
MySQL — base de datos
Maven — gestor de dependencias
JDBC — conexión a la base de datos
Git + GitHub — control de versiones


📦 Dependencias Maven
DependenciaVersiónUsomysql-connector-j8.3.0Conexión a MySQLitextpdf5.5.13.3Exportar reportes a PDF

📊 Módulos del sistema

Login — autenticación con roles (Admin / Cajero)
Productos — CRUD completo con búsqueda en tiempo real
Ventas — carrito de compras, factura y cálculo automático de totales
Inventario — control de stock automático con alertas de bajo inventario
Clientes — registro e historial de compras
Reportes — ventas del día, productos más vendidos, exportar CSV y PDF


⚙️ Requisitos previos
Antes de correr el proyecto asegúrate de tener instalado:

Java JDK 17+
Maven 3.8+
MySQL 8.0+
IntelliJ IDEA o NetBeans (cualquier IDE con soporte Maven)


🚀 Cómo instalar y correr el proyecto
1. Clonar el repositorio
bashgit clone https://github.com/nombre-usuario/colmado-system.git
cd colmado-system
2. Crear la base de datos
Abrir MySQL Workbench (o cualquier cliente MySQL), abrir el archivo sql/colmado_db.sql y ejecutarlo completo. Eso crea la base de datos colmado_db con todas las tablas.
También puedes ejecutarlo desde la terminal:
bashmysql -u root -p < sql/colmado_db.sql
3. Crear el archivo de configuración
Crear el archivo src/main/resources/config.properties con tus datos de MySQL locales:
propertiesdb.url=jdbc:mysql://localhost:3306/colmado_db
db.user=root
db.password=TU_CONTRASEÑA_AQUI

⚠️ Este archivo está en el .gitignore — nunca se sube al repositorio. Cada integrante del equipo debe crearlo manualmente en su computadora.

4. Abrir el proyecto en el IDE

IntelliJ IDEA: File → Open → seleccionar la carpeta colmado-system → el IDE detecta el pom.xml automáticamente → click en "Trust Project"
NetBeans: File → Open Project → seleccionar la carpeta → abrir

Maven descargará las dependencias automáticamente la primera vez (requiere conexión a internet).
5. Correr el proyecto
Ejecutar la clase principal MainFrame.java ubicada en el paquete com.colmado.vista.

🌿 Flujo de trabajo con Git
main ← develop ← feature/mi-modulo
Reglas del equipo

✅ Siempre trabajar en tu rama feature/ propia
✅ Hacer commits pequeños y frecuentes con mensajes claros
✅ Hacer git pull origin develop al inicio de cada día
✅ Abrir un Pull Request hacia develop cuando termines algo
❌ Nunca hacer push directo a main o develop
❌ Nunca subir config.properties al repositorio

Comandos diarios
bash# Al inicio del día — traer cambios del equipo
git checkout develop
git pull
git checkout feature/mi-modulo
git merge develop

# Mientras trabajo
git add .
git commit -m "Descripción clara de lo que hice"
git push

🗂️ Estructura del proyecto
colmado-system/
├── pom.xml                          ← configuración Maven
├── README.md
├── sql/
│   └── colmado_db.sql               ← script de la base de datos
└── src/
    └── main/
        ├── java/
        │   └── com/colmado/
        │       ├── modelo/          ← Producto, Venta, Cliente, Usuario
        │       ├── dao/             ← ProductoDAO, VentaDAO, ClienteDAO
        │       ├── service/         ← ProductoService, VentaService...
        │       ├── vista/           ← paneles Swing, MainFrame
        │       └── util/            ← ConexionDB (Singleton)
        └── resources/
            └── config.properties   ← credenciales BD (NO subir a Git)

🗄️ Base de datos
Nombre: colmado_db
TablaDescripciónusuariosCuentas del sistema con roles Admin y CajeroproductosCatálogo con nombre, precio, stock y categoríaclientesRegistro de clientes con contactoventasCabecera de cada venta con fecha y totaldetalle_ventaProductos incluidos en cada venta
Usuario de prueba:

Usuario: admin
Contraseña: 1234
Rol: ADMIN


📐 Patrones de diseño aplicados

Singleton — ConexionDB garantiza una sola instancia de conexión a MySQL
DAO (Data Access Object) — separa la lógica de acceso a datos del resto del sistema
MVC — separación entre modelo, vista y lógica de negocio
Observer — notificaciones automáticas cuando el stock baja del mínimo


📋 Materia
Análisis y Diseño de Sistemas — ITLA
Proyecto Final — Programación en Java
