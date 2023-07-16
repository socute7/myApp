<?php
require_once "../database.php";

$id = $_GET['id'];

$q = "SELECT * FROM invoice WHERE id = $id limit 1";
$get = mysqli_query($conn, $q);
$row = mysqli_fetch_array($get);
$result = [
    "id" => $row['id'],
    "invoice" => $row['invoice'],
    "tujuan" => $row['tujuan'],
    "alamat" => $row['alamat'],
    "nama" => $row['nama'],
    "no_hp" => $row['no_hp'],
    "berat_total" => $row['berat_total'],
    "harga_total" => $row['harga_total'],
    "kurir" => $row['kurir'],
    "harga_ongkir" => $row['harga_ongkir'],
    "status" => $row['status'],
    "tanggal" => $row['tanggal'],
];

echo json_encode([
    'data' => $result
]);
