<?php
require_once "../database.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $id = $_GET['id'];
    $password = md5($_POST['password']);
    $response = [];

    $q = "update user set password='$password' where id=$id";

    $exec = mysqli_query($conn, $q);
    if ($exec){
        $response['value'] = 1;
        $response['message'] = "Sukses";
    } else {
        $response['value'] = 0;
        $response['message'] = "Gagal";
    }

    echo json_encode($response);
}
