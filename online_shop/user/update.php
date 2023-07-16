<?php
require_once "../database.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_GET['id'];
    $nama = $_POST['nama'];
    $username = $_POST['username'];
    $umur = $_POST['umur'];
    $alamat = $_POST['alamat'];
  $gambar = isset($_POST['gambar'])? $_POST['gambar']:null;
  

    $response = [];

   $q = "update user set nama='$nama', username='$username', umur=$umur, alamat='$alamat', gambar='$gambar' where id=$id";    

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
