<?php
header('Content-Type: application/json');
include '../Conexion.php';

$data = json_decode(file_get_contents('php://input'), true);

if ($data === null) {
    echo json_encode(['success' => false, 'message' => 'JSON mal formado o vacío']);
    $conn->close();
    exit;
}

$id = intval($data['id'] ?? 0);
$informeId = intval($data['informeId'] ?? 0);
$corrida = $conn->real_escape_string($data['corrida'] ?? '');
$tren = $conn->real_escape_string($data['tren'] ?? '');
$llega = $conn->real_escape_string($data['llega'] ?? '');
$sale = $conn->real_escape_string($data['sale'] ?? '');
$operador = $conn->real_escape_string($data['operador'] ?? '');
$observaciones = $conn->real_escape_string($data['observaciones'] ?? '');

if ($id === 0) {
    echo json_encode(['success' => false, 'message' => 'ID es obligatorio para actualizar']);
    $conn->close();
    exit;
}

function calcularIntervalo($horaAnterior, $horaActual) {
    $t1 = strtotime($horaAnterior);
    $t2 = strtotime($horaActual);

    if ($t1 === false || $t2 === false) {
        return "00:00:00";
    }

    if ($t2 < $t1) {
        //Cruce de medianoche
        $t2 += 24 * 3600;
    }

    $diff = $t2 - $t1;
    return gmdate("H:i:s", $diff);
}

//Busca el registro anterior por ID y mismo informeId
$sqlAnterior = "SELECT sale FROM InformeDetalleXochimilco   WHERE informeId = $informeId AND id < $id ORDER BY id DESC LIMIT 1";

$resultAnterior = $conn->query($sqlAnterior);

if ($resultAnterior && $resultAnterior->num_rows > 0) {
    $rowAnterior = $resultAnterior->fetch_assoc();
    $saleAnterior = $rowAnterior['sale'];

    //Calcula intervalo entre la salida anterior y la salida actual
    $intervalo = calcularIntervalo($saleAnterior, $sale);
} else {
    //Si no hay registro anterior, intervalo por defecto
    $intervalo = "00:00:00";
}

$sql = "UPDATE InformeDetalleXochimilco SET 
            corrida = '$corrida',
            tren = '$tren',
            llega = '$llega',
            sale = '$sale',
            operador = '$operador',
            observaciones = '$observaciones',
            intervalo = '$intervalo'
        WHERE id = $id";

if ($conn->query($sql)) {
    echo json_encode(['success' => true, 'message' => 'Detalle actualizado con éxito', 'intervalo' => $intervalo]);
} else {
    echo json_encode(['success' => false, 'message' => 'Error al actualizar: ' . $conn->error]);
}

$conn->close();
?>
