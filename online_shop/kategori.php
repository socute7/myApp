<?php
require_once "database.php";

if (!$conn) {
    die('Koneksi database gagal: ' . mysqli_connect_error());
}

// Query untuk mendapatkan data kategori
$query = "SELECT * FROM kategori";
$result = mysqli_query($conn, $query);

// Membuat array kosong untuk menyimpan data kategori
$kategori = array();

// Mengambil data kategori dari hasil query
while ($row = mysqli_fetch_assoc($result)) {
    $kategori[] = $row;
}

// Mengubah array menjadi format JSON
$json_kategori = json_encode($kategori);

// Menampilkan data JSON
echo $json_kategori;

// Menutup koneksi database
mysqli_close($koneksi);
?>
