<?php
require_once "../database.php";

$q = "SELECT jual.id, user.username, produk.nama, produk.harga, invoice.tanggal FROM jual LEFT JOIN invoice ON jual.invoice_id = invoice.id LEFT JOIN produk ON jual.produk_id = produk.id LEFT JOIN user ON invoice.user_id = user.id order by tanggal desc";
$get = mysqli_query($conn, $q);
$result = [];
while ($row = mysqli_fetch_array($get)) {
    array_push($result, [
        "id" => $row['id'],
        "username" => $row['username'],
        "nama" => $row['nama'],
        "harga" => $row['harga'],
        "tanggal" => $row['tanggal']
    ]);
}

echo json_encode([
    'data' => $result
]);
