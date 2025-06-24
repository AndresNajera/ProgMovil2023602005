<?php
header('Content-Type: application/json');
include '../Conexion.php';

$informeId = isset($_GET['informeId']) ? intval($_GET['informeId']) : 0;

$sql = "SELECT * FROM InformeDetalleXochimilco WHERE informeId = $informeId";
$result = $conn->query($sql);

$detalles = [];

if ($result && $result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $detalles[] = $row;
    }
}

echo json_encode($detalles);
$conn->close();
?>
