<?php
header('Content-Type: application/json');
include '../Conexion.php';

$data = json_decode(file_get_contents('php://input'), true);

if (!$data || !isset($data['id'])) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Falta el ID del informe']);
    exit;
}

$id = intval($data['id']); //Validar que sea número entero

$sql = "DELETE FROM informeXochimilco WHERE id = $id";

if (mysqli_query($conn, $sql)) {
    if (mysqli_affected_rows($conn) > 0) {
        echo json_encode(['success' => true, 'message' => 'Informe eliminado']);
    } else {
        http_response_code(404);
        echo json_encode(['success' => false, 'message' => 'Informe no encontrado']);
    }
} else {
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => mysqli_error($conn)]);
}
?>
