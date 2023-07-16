<?php
session_start();
require_once './_top.php';
require_once './database.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    // Mengambil data dari form
    $nama_produk = $_POST['nama_produk'];
    $deskripsi = $_POST['deskripsi'];
    $harga = $_POST['harga'];
    $berat = $_POST['berat'];
    $stok = $_POST['stok'];
    $kategori = $_POST['kategori'];

    // Memproses file gambar (jika ada)
    $gambar = null;
    if (isset($_FILES['gambar']) && $_FILES['gambar']['error'] === UPLOAD_ERR_OK) {
        $gambar_name = $_FILES['gambar']['name'];
        $gambar_tmp = $_FILES['gambar']['tmp_name'];
        $gambar_ext = pathinfo($gambar_name, PATHINFO_EXTENSION);
        $gambar = uniqid() . '.' . $gambar_ext;
        move_uploaded_file($gambar_tmp, './gambar/' . $gambar);
    }

    // Menambahkan data produk ke tabel "produk"
    $sql = "INSERT INTO produk (nama, deskripsi, harga, berat, stok, gambar, kategori) 
            VALUES ('$nama_produk', '$deskripsi', $harga, $berat, $stok, '$gambar', '$kategori')";

    if (mysqli_query($conn, $sql)) {
        $_SESSION['info'] = [
            'status' => 'success',
            'message' => 'Berhasil menambah data'
        ];
        header('Location: ./index.php');
        exit;
    } else {
        $_SESSION['info'] = [
            'status' => 'failed',
            'message' => mysqli_error($conn)
        ];
        header('Location: ./index.php');
        exit;
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Form Tambah Produk</title>
</head>
<body>
    <h1>Form Tambah Produk</h1>

    <form method="post" action="<?php echo $_SERVER["PHP_SELF"]; ?>" enctype="multipart/form-data">
        <div>
            <label for="nama_produk">Nama Produk:</label>
          <div class="col-lg-3 col-md-6 col-sm-6 col-12">
            <input type="text" name="nama_produk" id="nama_produk" required>
        </div>
        <br>

        <div>
            <label for="deskripsi">Deskripsi:</label>
          <div class="col-lg-3 col-md-6 col-sm-6 col-12">
            <textarea name="deskripsi" id="deskripsi" required></textarea>
        </div>
        <br>

        <div>
            <label for="harga">Harga:</label>
          <div class="col-lg-3 col-md-6 col-sm-6 col-12">
            <input type="text" name="harga" id="harga" required>
        </div>
        <br>

        <div>
            <label for="berat">Berat:</label>
          <div class="col-lg-3 col-md-6 col-sm-6 col-12">
            <input type="number" name="berat" id="berat" required>
        </div>
        <br>

        <div>
            <label for="stok">Stok:</label>
          <div class="col-lg-3 col-md-6 col-sm-6 col-12">
            <input type="number" name="stok" id="stok" required>
        </div>
        <br>

        <div>
            <label for="kategori">Kategori:</label>
            <select class="form-control" name="kategori" id="kategori" required>
                <option value="">--Pilih Kategori--</option>
                <option value="reel">Reel</option>
                <option value="joran">Joran</option>
                <option value="pelampung">Pelampung</option>
                <option value="senar">Senar</option>
            </select>
        </div>
        <br>

        <div>
            <label for="gambar">Gambar:</label>
            <input type="file" name="gambar" id="gambar">
        </div>
        <br>

        <div>
            <input type="submit" value="Tambah Produk">
        </div>
    </form>
</body>
</html>

<?php
require_once './_bottom.php';
?>
