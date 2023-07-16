<?php
require_once "../database.php";

date_default_timezone_set("Asia/Jakarta");
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $invoice = "INV-" . date("YmdHms");
    $tujuan = $_POST['tujuan'];
    $alamat = $_POST['alamat'];
    $nama = $_POST['nama'];
    $no_hp = $_POST['no_hp'];
    $berat_total = $_POST['berat_total'];
    $harga_total = $_POST['harga_total'];
    $kurir = $_POST['kurir'];
    $harga_ongkir = $_POST['harga_ongkir'];
    $status = 0;
    $user_id = $_POST['user_id'];
    $tanggal = date("Y-m-d H:m:s");
    $response = [];

    try {
        $conn->begin_transaction();

        $q = "insert into invoice (
                     invoice,
                     tujuan,
                     alamat,
                     nama,
                     no_hp,
                     berat_total,
                     harga_total,
                     kurir,
                     harga_ongkir,
                     status,
                     user_id,
                     tanggal
) values (
          '$invoice',
          '$tujuan',
          '$alamat',
          '$nama',
          '$no_hp',
          $berat_total,
          $harga_total,
          '$kurir',
          $harga_ongkir,
          $status,
          $user_id,
          '$tanggal'
)";

        $store = $conn->query($q);
        $id = $conn->insert_id;

        

        $conn->commit();
        $response['value'] = 1;
        $response['id'] = $id;
        $response['message'] = "Sukses";
    } catch (Exception $e) {
        $conn->rollback();
        $response['value'] = 0;
        $response['message'] = "Gagal";
    }

    echo json_encode($response);
}
