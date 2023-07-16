<?php

require_once "../database.php";

$q = "select id, nama, username, umur, alamat,gambar from user where role=0";
$get = mysqli_query($conn, $q);
$result = [];
while ($row = mysqli_fetch_array($get)) {
    array_push($result, [
        "id" => $row['id'],
        "nama" => $row['nama'],
        "username" => $row['username'],
        "umur" => $row['umur'],
        "alamat" => $row['alamat'],
      "gambar" => $row['gambar']
    ]);
}

echo json_encode([
    'data' => $result
]);
