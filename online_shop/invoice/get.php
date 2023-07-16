<?php
require_once "../database.php";

$user_id = $_GET['user_id'];

$q = "SELECT id, invoice, status, tanggal from invoice WHERE user_id = $user_id and status=1 order by tanggal desc";
$get = mysqli_query($conn, $q);
$result = [];
while ($row = mysqli_fetch_array($get)) {
    array_push($result, [
        "id" => $row['id'],
        "invoice" => $row['invoice'],
        "status" => $row['status'],
        "tanggal" => $row['tanggal']
    ]);
}

echo json_encode([
    'data' => $result
]);
