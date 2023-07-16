<?php
session_start();
require_once '../database.php';

$id = $_POST["id"];
$nama_produk = $_POST["nama_produk"];
        $deskripsi = $_POST["deskripsi"];
        $harga = $_POST["harga"];
        $stok = $_POST["stok"];

$query = mysqli_query($conn, "insert into nilai (id, nama_produk, deskripsi, harga, stok) value('$id', '$nama_produk', '$deskripsi', '$harga', '$stok')");

if ($query) {
  $_SESSION['info'] = [
    'status' => 'success',
    'message' => 'Berhasil menambah data'
  ];
  header('Location: ./index.php');
} else {
  $_SESSION['info'] = [
    'status' => 'failed',
    'message' => mysqli_error($conn)
  ];
  header('Location: ./index.php');
}
