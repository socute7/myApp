<?php
require_once "../database.php";

$start = $_GET['start'];
$end = $_GET['end'];

$q = "SELECT jual.id, user.username, produk.nama, produk.harga, invoice.tanggal FROM jual LEFT JOIN invoice ON jual.invoice_id = invoice.id LEFT JOIN produk ON jual.produk_id = produk.id LEFT JOIN user ON invoice.user_id = user.id WHERE invoice.tanggal >= '$start 00:00:00' AND invoice.tanggal <= '$end 23:59:59'";
$get = mysqli_query($conn, $q);
$result = [];
if ($get){
    while ($row = mysqli_fetch_array($get)) {
        array_push($result, [
            "id" => $row['id'],
            "username" => $row['username'],
            "nama" => $row['nama'],
            "harga" => $row['harga'],
            "tanggal" => $row['tanggal']
        ]);
    }
}

echo json_encode([
    'data' => $result
]);
