<?php
require_once "../database.php";

$id = $_GET['id'];

$q = "SELECT * from user WHERE id=$id";
$get = mysqli_query($conn, $q);
$row = mysqli_fetch_array($get);
$result = [
	'id' => $row['id'],
	'nama' => $row['nama'],
	'username' => $row['username'],
	'umur' => $row['umur'],
	'alamat' => $row['alamat'],
  	'gambar' => $row['gambar']
];

echo json_encode([
    'data' => $result
]);
