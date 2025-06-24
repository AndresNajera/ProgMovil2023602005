<?php
include '../Conexion.php';
ini_set('display_errors', 1);
error_reporting(E_ALL);

//Conexión a la base de datos
$host = "localhost";
$user = "u962863782_userste";
$password = "Steadmin77";
$db = "u962863782_ste";

$conn = mysqli_connect($host, $user, $password, $db);
if (!$conn) {
    die(json_encode(["status" => "error", "message" => "Error de conexión: " . mysqli_connect_error()]));
}

//Obtener JSON enviado desde la app
$input = file_get_contents('php://input');
$data = json_decode($input, true);

if (!$data) {
    echo json_encode(["status" => "error", "message" => "Datos JSON inválidos"]);
    exit;
}

$correo = $data['correo'] ?? '';
$password = $data['password'] ?? '';

if (empty($correo) || empty($password)) {
    echo json_encode(["status" => "error", "message" => "Correo y contraseña son obligatorios"]);
    exit;
}

//Buscar usuario por correo
$sql = "SELECT * FROM usuarios WHERE correo = ?";
$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "s", $correo);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);

if ($row = mysqli_fetch_assoc($result)) {
    if (password_verify($password, $row['password'])) {
        echo json_encode([
            "status" => "success",
            "message" => "Login correcto",
            "user" => [
                "nombreJefe" => $row['nombreJefe'],
                "expediente" => $row['expediente'],
                "correo" => $row['correo']
            ]
        ]);
    } else {
        echo json_encode(["status" => "error", "message" => "Contraseña incorrecta"]);
    }
} else {
    echo json_encode(["status" => "error", "message" => "Usuario no encontrado"]);
}

mysqli_stmt_close($stmt);
mysqli_close($conn);
?>
