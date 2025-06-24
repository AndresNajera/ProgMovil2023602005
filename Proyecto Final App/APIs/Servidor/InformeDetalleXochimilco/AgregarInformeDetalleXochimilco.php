<?php
header('Content-Type: application/json');
include '../Conexion.php';

$data = json_decode(file_get_contents('php://input'), true);

if ($data === null) {
    echo json_encode(['success' => false, 'message' => 'JSON mal formado o vacío']);
    $conn->close();
    exit;
}

$informeId = intval($data['informeId'] ?? 0);
$corrida = $conn->real_escape_string($data['corrida'] ?? '');
$tren = $conn->real_escape_string($data['tren'] ?? '');
$llega = $conn->real_escape_string($data['llega'] ?? '');
$sale = $conn->real_escape_string($data['sale'] ?? '');
$operador = $conn->real_escape_string($data['operador'] ?? '');
$observaciones = $conn->real_escape_string($data['observaciones'] ?? '');

//1.Consultar última salida para este informeId
$sqlLastSale = "SELECT sale FROM InformeDetalleXochimilco WHERE informeId = $informeId ORDER BY id DESC LIMIT 1";
$result = $conn->query($sqlLastSale);

$lastSale = null;
if ($result && $result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $lastSale = $row['sale']; //formato esperado HH:mm:ss
}

//2.Calcular intervalo (en PHP)
function calcularIntervalo($ultimaSalida, $nuevaSalida) {
    $time1 = strtotime($ultimaSalida);
    $time2 = strtotime($nuevaSalida);

    if ($time1 === false || $time2 === false) return "00:00:00";

    if ($time2 < $time1) {
        //Cruce de medianoche
        $time2 += 24 * 3600; //suma 24h
    }
    $diff = $time2 - $time1;

    return gmdate("H:i:s", $diff);
}

$intervalo = "00:00:00";
if ($lastSale !== null) {
    $intervalo = calcularIntervalo($lastSale, $sale);
}

//3.Insertar con intervalo calculado
$sql = "INSERT INTO InformeDetalleXochimilco 
    (informeId, corrida, tren, llega, sale, intervalo, operador, observaciones) 
    VALUES 
    ($informeId, '$corrida', '$tren', '$llega', '$sale', '$intervalo', '$operador', '$observaciones')";

if ($conn->query($sql)) {
    echo json_encode(['success' => true, 'message' => 'Detalle guardado con éxito', 'intervalo' => $intervalo]);
} else {
    echo json_encode(['success' => false, 'message' => 'Error al guardar: ' . $conn->error]);
}

$conn->close();
?>
