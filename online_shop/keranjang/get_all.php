<?php
require_once "../database.php";

$user_id = $_GET['user_id'];

$q = "SELECT keranjang.id,produk_id, nama,sum(harga) as total, berat, gambar,sum(quantity) as qty FROM keranjang LEFT JOIN produk ON keranjang.produk_id = produk.id WHERE user_id = $user_id group by produk_id";
$get = mysqli_query($conn, $q);
$result = [];
while ($row = mysqli_fetch_array($get)) {
    array_push($result, [
        "id" => $row['id'],
        "produk_id" => $row['produk_id'],
        "nama" => $row['nama'],
        "harga" => $row['total'],
        "berat" => $row['berat'],
        "gambar" => $row['gambar'],
      "quantity" => $row['qty']
    ]);
}

echo json_encode([
    'data' => $result
]);
