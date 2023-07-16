<?php
require_once "../database.php";

if ($_SERVER['REQUEST_METHOD'] == "POST") {
    $nama = $_POST['nama'];
    $deskripsi = $_POST['deskripsi'];
    $harga = $_POST['harga'];
    $berat = $_POST['berat'];
    $stok = $_POST['stok'];

    $gambar = basename($_FILES['gambar']['name']);
    $file_name = pathinfo($gambar, PATHINFO_FILENAME);
    $file_ext = pathinfo($gambar, PATHINFO_EXTENSION);
    $new_gambar = $file_name . "-" . date("Ymdhms") . "." . $file_ext;

    $response = [];

    $upload = move_uploaded_file($_FILES['gambar']['tmp_name'], "../gambar/{$new_gambar}");
    if ($upload) {
        $query = "insert into produk (nama, deskripsi, harga, berat, stok, gambar) values ('{$nama}','{$deskripsi}',{$harga}, $berat, {$stok},'{$new_gambar}')";

        $sql = mysqli_query($conn, $query);

        if ($sql) {
            $response['value'] = 1;
            $response['message'] = "Sukses create data";
        } else {
            $response['value'] = 0;
            $response['message'] = "Gagal create data";
        }
    } else {
        $response['value'] = 0;
        $response['message'] = "Gagal create data";
    }

    echo json_encode($response);

}