<?php
require_once "../database.php";

$id = $_GET['id'];
$response = [];

$q = "delete from keranjang where id=$id";

$delete = mysqli_query($conn, $q);
if ($delete){
    $response['value'] = 1;
    $response['message'] = "Sukses";
} else {
    $response['value'] = 0;
    $response['message'] = "Gagal";
}

echo json_encode($response);

