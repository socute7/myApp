<?php
require_once "_top.php";
require_once "database.php";

function updateCounts($conn) {
    $invoice = mysqli_query($conn, "SELECT COUNT(*) FROM invoice");
    $jual = mysqli_query($conn, "SELECT COUNT(*) FROM jual");
    $produk = mysqli_query($conn, "SELECT COUNT(*) FROM produk");

    $total_invoice = 0;
    $total_jual = 0;
    $total_produk = 0;

    if ($invoice && $jual && $produk) {
        $total_invoice = mysqli_fetch_array($invoice)[0];
        $total_jual = mysqli_fetch_array($jual)[0];
        $total_produk = mysqli_fetch_array($produk)[0];
    }
    
    return [
        'total_invoice' => $total_invoice,
        'total_jual' => $total_jual,
        'total_produk' => $total_produk
    ];
}

// Call the function to update the counts
$countData = updateCounts($conn);
$total_invoice = $countData['total_invoice'];
$total_jual = $countData['total_jual'];
$total_produk = $countData['total_produk'];
?>

<section class="section">
  <div class="section-header">
    <h1>Dashboard</h1>
  </div>
  <div class="column">
    <div class="row">
      <div class="col-lg-3 col-md-6 col-sm-6 col-12">
        <div class="card card-statistic-1">
          <div class="card-icon bg-primary">
            <i class="far fa-user"></i>
          </div>
          <div class="card-wrap">
            <div class="card-header">
              <h4>Invoice</h4>
            </div>
            <div class="card-body">
              <?= $total_invoice ?>
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-3 col-md-6 col-sm-6 col-12">
        <div class="card card-statistic-1">
          <div class="card-icon bg-danger">
            <i class="far fa-user"></i>
          </div>
          <div class="card-wrap">
            <div class="card-header">
              <h4>Penjualan</h4>
            </div>
            <div class="card-body">
              <?= $total_jual ?>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-lg-3 col-md-6 col-sm-6 col-12">
        <div class="card card-statistic-1">
          <div class="card-icon bg-success">
            <i class="far fa-newspaper"></i>
          </div>
          <div class="card-wrap">
            <div class="card-header">
              <h4>Produk</h4>
            </div>
            <div class="card-body">
              <?= $total_produk ?>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

<?php
require_once '_bottom.php';
?>
