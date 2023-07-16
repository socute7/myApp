<?php
require_once "../database.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $nama = $_POST['nama'];
    $username = $_POST['username'];
    $password = md5($_POST['password']);
    $role = 0;
    $umur = $_POST['umur'] ?? 0;
    $alamat = $_POST['alamat'] ?? null;
  $gambar = NULL;
    $response = [];

    $q = "insert into user (nama, username, password, role, umur, alamat,gambar) values ('{$nama}', '{$username}', '{$password}', {$role}, {$umur}, '{$alamat}', '{$gambar}')";

    $store = mysqli_query($conn, $q);
    if ($store){
        $response['value'] = 1;
        $response['message'] = "Sukses";
    } else {
        $response['value'] = 0;
        $response['message'] = "Gagal";
    }

    echo json_encode($response);
}
