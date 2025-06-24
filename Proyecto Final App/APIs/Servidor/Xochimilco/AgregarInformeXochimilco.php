<?php
header('Content-Type: application/json');
include '../Conexion.php';

$data = json_decode(file_get_contents('php://input'), true);

if (!$data || !isset($data['expedienteJefe'], $data['turno'], $data['fecha'])) {
    echo json_encode(['success' => false, 'message' => 'Faltan datos obligatorios']);
    exit;
}

$expedienteJefe = mysqli_real_escape_string($conn, $data['expedienteJefe']);
$turno = mysqli_real_escape_string($conn, $data['turno']);
$fecha = date('Y-m-d', strtotime($data['fecha']));

//Verifica que el expediente exista en usuarios
$checkExpedienteSql = "SELECT id FROM usuarios WHERE expediente = '$expedienteJefe' LIMIT 1";
$result = mysqli_query($conn, $checkExpedienteSql);

if (mysqli_num_rows($result) == 0) {
    echo json_encode(['success' => false, 'message' => 'Expediente no registrado']);
    exit;
}

//Verifica que no exista un informe con el mismo expedienteJefe, fecha y turno
$checkInformeSql = "SELECT id FROM informeXochimilco WHERE expedienteJefe = '$expedienteJefe' AND fecha = '$fecha' AND turno = '$turno' LIMIT 1";
$resultInforme = mysqli_query($conn, $checkInformeSql);

if (mysqli_num_rows($resultInforme) > 0) {
    echo json_encode(['success' => false, 'message' => 'Ya existe un informe para este expediente, fecha y turno']);
    exit;
}

$sql = "INSERT INTO informeXochimilco (terminal, expedienteJefe, turno, fecha) VALUES ('xochimilco', '$expedienteJefe', '$turno', '$fecha')";

//Debug previo a insert
error_log("Insertando informe: expediente=$expedienteJefe, turno=$turno, fecha=$fecha");

if (mysqli_query($conn, $sql)) {
    error_log("Informe insertado correctamente");
    echo json_encode(['success' => true, 'message' => 'Informe guardado']);
} else {
    $error = mysqli_error($conn);
    error_log("Error insertando informe: $error");
    echo json_encode(['success' => false, 'message' => $error]);
}
?>
