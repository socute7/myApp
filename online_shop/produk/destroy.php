<?php
require_once "../database.php";

$id = $_GET['id'];
$response = [];

$produk = mysqli_query($conn, "select gambar from produk where id=$id");
$produk_data = mysqli_fetch_array($produk);
if ($produk_data) {
    if (file_exists("../gambar/" . $produk_data['gambar'])) unlink("../gambar/" . $produk_data['gambar']);

    $q = "delete from produk where id=$id";

    $delete = mysqli_query($conn, $q);
    if ($delete) {
        $response['value'] = 1;
        $response['message'] = "Sukses";
    } else {
        $response['value'] = 0;
        $response['message'] = "Gagal";
    }
} else {
    $response['value'] = 0;
    $response['message'] = "Null";
}

echo json_encode($response);