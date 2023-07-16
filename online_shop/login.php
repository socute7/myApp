<?php
require_once "database.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username = $_POST['username'];
    $password = md5($_POST['password']);
    $response = [];

    $q = "select * from user where username='{$username}' and password='{$password}' limit 1";

    $get = mysqli_query($conn, $q);
    $row = mysqli_fetch_array($get);
    if ($row){
        $response['value'] = 1;
        $response['id'] = $row['id'];
        $response['nama'] = $row['nama'];
        $response['username'] = $row['username'];
        $response['role'] = $row['role'];
      	$response['alamat'] = $row['alamat'];
    } else {
        $response['value'] = 0;
        $response['message'] = "Gagal";
    }

    echo json_encode($response);
}