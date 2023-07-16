<!DOCTYPE html>
<html>
<head>
  <title>Invoice</title>
  <style>
    table {
      width: 100%;
      border-collapse: collapse;
    }

    table, th, td {
      border: 1px solid black;
    }
  </style>
</head>
<body>
  <h1>Invoice</h1>
  <?php
   require_once './_top.php';
require_once './database.php';

    // Query untuk mendapatkan data invoice dari tabel
    $sql = "SELECT * FROM invoice";
    $result = $conn->query($sql);

    // Cek apakah ada data invoice
    if ($result->num_rows > 0) {
      echo "<table>";
      echo "<tr>";
      echo "<th>ID</th>";
      echo "<th>Invoice</th>";
      echo "<th>Tujuan</th>";
      echo "<th>Alamat</th>";
      echo "<th>Nama</th>";
      echo "<th>No HP</th>";
      echo "<th>Berat Total</th>";
      echo "<th>Harga Total</th>";
      echo "<th>Kurir</th>";
      echo "<th>Harga Ongkir</th>";
      echo "<th>Tanggal</th>";
      echo "</tr>";

      // Output data invoice
      while($row = $result->fetch_assoc()) {
        echo "<tr>";
        echo "<td>".$row["id"]."</td>";
        echo "<td>".$row["invoice"]."</td>";
        echo "<td>".$row["tujuan"]."</td>";
        echo "<td>".$row["alamat"]."</td>";
        echo "<td>".$row["nama"]."</td>";
        echo "<td>".$row["no_hp"]."</td>";
        echo "<td>".$row["berat_total"]."</td>";
        echo "<td>".$row["harga_total"]."</td>";
        echo "<td>".$row["kurir"]."</td>";
        echo "<td>".$row["harga_ongkir"]."</td>";
        echo "<td>".$row["tanggal"]."</td>";
        echo "</tr>";
      }
      echo "</table>";
    } else {
      echo "Tidak ada data invoice.";
    }

    // Tutup koneksi ke database
    $conn->close();
  ?>
</body>
</html>
<?php
require_once './_bottom.php';
?>
