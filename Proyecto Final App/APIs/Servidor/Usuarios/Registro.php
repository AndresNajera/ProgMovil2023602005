<?php
include '../Conexion.php';
// Mostrar errores para debugging 
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

//Configuración conexión
$host = "localhost";
$user = "u962863782_userste";
$password = "Steadmin77";
$db = "u962863782_ste";

$conn = mysqli_connect($host, $user, $password, $db);
if (!$conn) {
    die(json_encode([
        "status" => "error",
        "message" => "Error de conexión: " . mysqli_connect_error(),
        "errorCode" => "DB_CONNECTION_ERROR"
    ]));
}

$input = file_get_contents('php://input');
$data = json_decode($input, true);

if (!$data) {
    echo json_encode([
        "status" => "error",
        "message" => "Datos JSON inválidos o vacíos",
        "errorCode" => "INVALID_JSON"
    ]);
    exit;
}

//Datos del usuario
$nombreJefe = $data['nombreJefe'] ?? '';
$expediente = $data['expediente'] ?? '';
$correo = $data['correo'] ?? '';
$password = $data['password'] ?? '';
$ultimaModificacion = date('Y-m-d H:i:s');

//Validación básica
if (empty($nombreJefe) || empty($expediente) || empty($correo) || empty($password)) {
    echo json_encode([
        "status" => "error",
        "message" => "Faltan datos obligatorios",
        "errorCode" => "MISSING_FIELDS"
    ]);
    exit;
}

//Validar si el correo ya existe
$queryCorreo = "SELECT id FROM usuarios WHERE correo = ?";
$stmtCorreo = mysqli_prepare($conn, $queryCorreo);
mysqli_stmt_bind_param($stmtCorreo, "s", $correo);
mysqli_stmt_execute($stmtCorreo);
mysqli_stmt_store_result($stmtCorreo);

if (mysqli_stmt_num_rows($stmtCorreo) > 0) {
    echo json_encode([
        "status" => "error",
        "message" => "El correo ya está registrado",
        "errorCode" => "EMAIL_ALREADY_EXISTS"
    ]);
    mysqli_stmt_close($stmtCorreo);
    mysqli_close($conn);
    exit;
}
mysqli_stmt_close($stmtCorreo);

//Validar si el expediente ya existe
$queryExp = "SELECT id FROM usuarios WHERE expediente = ?";
$stmtExp = mysqli_prepare($conn, $queryExp);
mysqli_stmt_bind_param($stmtExp, "s", $expediente);
mysqli_stmt_execute($stmtExp);
mysqli_stmt_store_result($stmtExp);

if (mysqli_stmt_num_rows($stmtExp) > 0) {
    echo json_encode([
        "status" => "error",
        "message" => "El número de expediente ya está registrado",
        "errorCode" => "EXPEDIENTE_ALREADY_EXISTS"
    ]);
    mysqli_stmt_close($stmtExp);
    mysqli_close($conn);
    exit;
}
mysqli_stmt_close($stmtExp);

//Encriptar contraseña
$password_hash = password_hash($password, PASSWORD_DEFAULT);

//Insertar usuario nuevo
$sql = "INSERT INTO usuarios (nombreJefe, expediente, correo, password, ultimaModificacion) VALUES (?, ?, ?, ?, ?)";
$stmt = mysqli_prepare($conn, $sql);

if (!$stmt) {
    echo json_encode([
        "status" => "error",
        "message" => "Error en la preparación de la consulta",
        "errorCode" => "PREPARE_FAILED"
    ]);
    exit;
}

mysqli_stmt_bind_param($stmt, "sssss", $nombreJefe, $expediente, $correo, $password_hash, $ultimaModificacion);

if (mysqli_stmt_execute($stmt)) {
    echo json_encode([
        "status" => "success",
        "message" => "Usuario registrado correctamente"
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Error al registrar usuario",
        "errorCode" => "REGISTER_FAILED"
    ]);
}

mysqli_stmt_close($stmt);
mysqli_close($conn);
?>
