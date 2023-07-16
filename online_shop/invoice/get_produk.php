<?
$q = "SELECT i.nama AS nama_invoice, i.alamat AS alamat_invoice, p.nama AS nama_produk, p.harga AS harga_produk
      FROM invoice AS i
      LEFT JOIN jual AS j ON i.id = j.invoice_id
      LEFT JOIN produk AS p ON j.produk_id = p.id
      LEFT JOIN user AS u ON i.user_id = u.id
      WHERE j.invoice_id = $id";

$get = mysqli_query($conn, $q);
$result = [];
while ($row = mysqli_fetch_array($get)) {
    array_push($result, [
        "nama_invoice" => $row['nama_invoice'],
        "alamat_invoice" => $row['alamat_invoice'],
        "nama_produk" => $row['nama_produk'],
        "harga_produk" => $row['harga_produk'],
    ]);
}

echo json_encode([
    'data' => $result
]);
