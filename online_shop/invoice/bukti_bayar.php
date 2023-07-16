<?php
require_once "../database.php";

if ($_SERVER['REQUEST_METHOD'] == "POST") {
    $id = $_GET['id'];
    // $gambar = basename($_FILES['bukti_bayar']['name']);
    // $file_name = pathinfo($gambar, PATHINFO_FILENAME);
    // $file_ext = pathinfo($gambar, PATHINFO_EXTENSION);
    // $new_gambar = $file_name . "-" . date("Ymdhms") . "." . $file_ext;

    // $response = [];

    // $upload = move_uploaded_file($_FILES['bukti_bayar']['tmp_name'], "../bukti-bayar/{$new_gambar}");
    $upload = 1;
    if ($upload) {
        $query = "update invoice set status=1 where id=$id";

        $sql = mysqli_query($conn, $query);

        if ($sql) {
            $response['value'] = 1;
            $response['message'] = "Sukses";
        } else {
            $response['value'] = 0;
            $response['message'] = "Gagal";
        }
    } else {
        $response['value'] = 0;
        $response['message'] = "Gagal";
    }

    echo json_encode($response);

}