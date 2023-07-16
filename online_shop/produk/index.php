<?php
require_once '../_top.php';
require_once '../database.php';

$result = mysqli_query($conn, "SELECT * FROM produk");
?>

<section class="section">
  <div class="section-header d-flex justify-content-between">
    <h1>Produk</h1>
    <a href="../create.php" class="btn btn-primary">Tambah Data</a>
  </div>
  <div class="row">
    <div class="col-12">
      <div class="card">
        <div class="card-body">
          <div class="table-responsive">
            <table class="table table-hover table-striped w-100" id="table-1">
              <thead>
                <tr class="text-center">
                  <th>No</th>
                  <th>Nama</th>
                  <th>Deskripsi</th>
                  <th>Harga</th>
                  <th>Berat</th>
                  <th>Stok</th>
                  <th>Gambar</th>
                  <th>Kategori</th>
                </tr>
              </thead>
              <tbody>
                <?php
                $no = 1;
                while ($data = mysqli_fetch_array($result)) :
                ?>

                  <input type="hidden" name="id" value="<?= $row['id'] ?>">
                  <tr class="text-center">
                    <td><?= $no ?></td>
                    <td><?= $data['nama'] ?></td>
                    <td><?= $data['deskripsi'] ?></td>
                    <td><?= $data['harga'] ?></td>
                    <td><?= $data['berat'] ?></td>
                    <td><?= $data['stok'] ?></td>
                    <td style="vertical-align: middle;"><img src="../gambar/<?= $data['gambar'] ?>" alt="Gambar Produk" style="max-width: 100px; max-height: 100px;"></td>

                    <td><?= $data['kategori'] ?></td>
                  </tr>

                <?php
                  $no++;
                endwhile;
                ?>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
</section>

<?php
require_once '../_bottom.php';
?>
<!-- Page Specific JS File -->
<?php
if (isset($_SESSION['info'])) :
  if ($_SESSION['info']['status'] == 'success') {
?>
    <script>
      iziToast.success({
        title: 'Sukses',
        message: `<?= $_SESSION['info']['message'] ?>`,
        position: 'topCenter',
        timeout: 5000
      });
    </script>
  <?php
  } else {
  ?>
    <script>
      iziToast.error({
        title: 'Gagal',
        message: `<?= $_SESSION['info']['message'] ?>`,
        timeout: 5000,
        position: 'topCenter'
      });
    </script>
<?php
  }

  unset($_SESSION['info']);
  $_SESSION['info'] = null;
endif;
?>
<script src="online_shop/assets/js/page/modules-datatables.js"></script>
