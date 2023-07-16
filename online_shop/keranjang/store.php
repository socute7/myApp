<?php
require_once "../database.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $user_id = $_POST['user_id'];
    $produk_id = $_POST['produk_id'];
    $response = [];

    $q = "insert into keranjang (user_id, produk_id,quantity) values ($user_id, $produk_id,1)";

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
