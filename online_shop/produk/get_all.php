<?php

require_once "../database.php";

$q = "select * from produk";
$get = mysqli_query($conn, $q);
$result = [];
while ($row = mysqli_fetch_array($get)) {
    array_push($result, [
        "id" => $row['id'],
        "nama" => $row['nama'],
        "deskripsi" => $row['deskripsi'],
        "harga" => $row['harga'],
        "berat" => $row['berat'],
        "stok" => $row['stok'],
        "gambar" => $row['gambar'],
      	"kategori" => $row['kategori']
    ]);
}

echo json_encode([
    'data' => $result
]);
