<?php
header('Content-Type: application/json');
include '../Conexion.php';

$data = json_decode(file_get_contents('php://input'), true);
$id = intval($data['id'] ?? 0);

if ($id > 0) {
    $sql = "DELETE FROM InformeDetalleXochimilco WHERE id = $id";

    if ($conn->query($sql)) {
        echo json_encode(['success' => true, 'message' => 'Detalle eliminado con éxito']);
    } else {
        echo json_encode(['success' => false, 'message' => 'Error al eliminar: ' . $conn->error]);
    }
} else {
    echo json_encode(['success' => false, 'message' => 'ID inválido']);
}

$conn->close();
?>
