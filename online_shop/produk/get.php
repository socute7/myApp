<?php
require_once "../database.php";

$id = $_GET['id'];

$q = "SELECT * from produk WHERE id=$id";
$get = mysqli_query($conn, $q);
$row = mysqli_fetch_array($get);
$result = [];
if ($row) {
	$result = [
		'id' => $row['id'],
		'nama' => $row['nama'],
		'deskripsi' => $row['deskripsi'],
		'harga' => $row['harga'],
		'berat' => $row['berat'],
		'stok' => $row['stok'],
		'gambar' => $row['gambar']
	];
}

echo json_encode([
    'data' => $result
]);
