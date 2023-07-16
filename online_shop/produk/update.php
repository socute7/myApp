<?php
require_once "../database.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_GET['id'];
    $nama = $_POST['nama'];
    $deskripsi = $_POST['deskripsi'];
    $harga = $_POST['harga'];
    $berat = $_POST['berat'];
    $stok = $_POST['stok'];
    $response = [];

    $produk = mysqli_query($conn, "select id from produk where id=$id");
    $produk_data = mysqli_fetch_array($produk);
    if ($produk_data) {
        $q = "update produk set nama='$nama', deskripsi='$deskripsi', harga=$harga, berat=$berat, stok=$stok where id=$id";

        $store = mysqli_query($conn, $q);
        if ($store){
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
}
