<?php
header('Content-Type: application/json');
include '../Conexion.php';

//Obtener parámetros
$terminal = isset($_GET['terminal']) ? mysqli_real_escape_string($conn, $_GET['terminal']) : 'xochimilco'; 
$expedienteJefe = isset($_GET['expedienteJefe']) ? mysqli_real_escape_string($conn, $_GET['expedienteJefe']) : null;
$turno = isset($_GET['turno']) ? mysqli_real_escape_string($conn, $_GET['turno']) : null;

$sql = "SELECT * FROM informeXochimilco WHERE terminal = '$terminal'";

if ($expedienteJefe) {
    $sql .= " AND expedienteJefe LIKE '%$expedienteJefe%'";
}
if ($turno) {
    $sql .= " AND turno = '$turno'";
}

$result = mysqli_query($conn, $sql);

$informes = [];
if ($result) {
    while ($row = mysqli_fetch_assoc($result)) {
        $informes[] = $row;
    }
    echo json_encode($informes);
} else {
    http_response_code(500);
    echo json_encode(['error' => mysqli_error($conn)]);
}
?>
