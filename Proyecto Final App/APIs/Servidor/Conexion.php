<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

header('Content-Type: application/json'); //Que sea JSON

$host = "localhost";
$user = "u962863782_userste";
$password = "Steadmin77";
$db = "u962863782_ste";

$conn = mysqli_connect($host, $user, $password, $db);

if (!$conn) {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Conexión fallida: " . mysqli_connect_error()
    ]);
    exit;
}

?>


