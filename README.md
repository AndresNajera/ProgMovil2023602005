STE 

Repositorio Final

Descripción: 
Es una app para el registro y control del Servicio de Trasnportes Electricos, para el Tren Ligero de la CDMX

Tecnologias:
Lenguaje: Kotlin
IDE: Android Studio 
Sincronizacion: Servidor de paga en Hostinger 

Requisitos:
Acceso a Internte
Servidor PHP con base de Datos
Android 7.0 o superior

Intalacion y Configuracion
Descargar y la Carpeta se llama Proyecto Final App
git clone https://github.com/AndresNajera/ProgMovil2023602005.git

Configurar Base de Datos CREATE TABLE usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombreJefe VARCHAR(255) NOT NULL,
  expediente VARCHAR(50) NOT NULL,
  correo VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  ultimaModificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
SELECT * FROM usuarios;
----------------------------------------------------------------------------------------------
CREATE TABLE informeTasquena (
  id INT AUTO_INCREMENT PRIMARY KEY,
  terminal VARCHAR(20) DEFAULT 'tasquena',  -- siempre 'tasquena'
  expedienteJefe VARCHAR(20),                -- expediente del jefe
  turno ENUM('A', 'B'),
  fecha DATE
);
SELECT * FROM informeTasquena;
---------------------------------------------------------------------------------------------
CREATE TABLE informeXochimilco (
  id INT AUTO_INCREMENT PRIMARY KEY,
  terminal VARCHAR(20) DEFAULT 'xochimilco',  -- siempre 'xochimilco'
  expedienteJefe VARCHAR(20),                  -- expediente del jefe
  turno ENUM('A', 'B'),
  fecha DATE
);

SELECT * FROM informeXochimilco;
--------------------------------------------------------------------------------------------
CREATE TABLE InformeDetalleTasquena (
  id INT AUTO_INCREMENT PRIMARY KEY,
  informeId INT,
  corrida VARCHAR(2),       -- '01', '02', ..., '18'
  tren VARCHAR(3),          -- código del tren
  llega TIME,               -- hora de llegada
  sale TIME,                -- hora de salida
  intervalo TIME,           -- diferencia entre salidas
  operador VARCHAR(5),      -- operador con 5 dígitos
  observaciones TEXT,
  FOREIGN KEY (informeId) REFERENCES informeTasquena(id) ON DELETE CASCADE
);

SELECT * FROM InformeDetalleTasquena;
--------------------------------------------------------------------------------------------
CREATE TABLE InformeDetalleXochimilco (
  id INT AUTO_INCREMENT PRIMARY KEY,
  informeId INT,
  corrida VARCHAR(2),       -- '01', '02', ..., '18'
  tren VARCHAR(3),          -- código del tren
  llega TIME,               -- hora de llegada
  sale TIME,                -- hora de salida
  intervalo TIME,           -- diferencia entre salidas
  operador VARCHAR(5),      -- operador con 5 dígitos
  observaciones TEXT,
  FOREIGN KEY (informeId) REFERENCES informeTasquena(id) ON DELETE CASCADE
);

SELECT * FROM InformeDetalleXochimilco;


Subir la API al servidor 
$host = "localhost";
$user = "u962863782_userste";
$password = "Steadmin77";
$db = "u962863782_ste";

Configurar la URL 
.baseURL("https://tudominio.com/")

Ejecutar la App en Android Studio
-Abre el proyecto en Android Studio.
-Conecta un dispositivo Android físico (API 26 o superior) o usa un emulador.
-Haz clic en Run para compilar y ejecutar la aplicación.

Funcionalidades Principales
-Registro, Login, 2 terminales por default, poder registrar y eliminar informes, y dentro de estos poder crear, editar, eliminar y actualizar registros de trenes y personalizacion

Gestión de Datos
-La base de datos se gestiona desde phpMyAdmin, alojada en Hostinger (es el backend de la aplicacion)
-Las operaciones CRUD (crear, leer, actualizar, eliminar) se manejan a través de peticiones HTTP a una API en PHP
-Los datos se transmiten en formato JSON entre el frontend (app móvil) y el backend (servidor)

Pruebas Realizadas
-Registro Exitoso de informes en distintos dispositivos
-Sincronización en tiempo real entre usuarios
-Edición y eliminación de registros e informes
-Validaciones previas a la eliminación de un registro o informe
-Scroll continuo para visualizar los registros
-Cambio de preferencias visuales (colores)

Mantenimiento y Actualizaciones
No requiere por que es un proyecto escolar

Contacto
Castillo Varela Regina Paulette - reginapaulette09@gmail.com
Najera Flores Andres - andresnajera.ipn13@gmail.com
