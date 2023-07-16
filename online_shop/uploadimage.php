<?php
$BASE_URL_IMAGE="./images/";
$filename="img".date("YmdHis").rand(,999).".jpg";

$res=array();
$kode="":
$pesan="";

if($_SERVER['REQUEST_METHOD']=="POST"){
    if($_FILES['$imageupload']){
        $dest=$BASE_URL_IMAGE.$filename
        if(move_uploaded_file($_FILES['imageupload']['tmp_name'], $dest)){
            $kode=1;
            $pesan="Upload Sukses";
        }else{
            $kode=0;
            $pesan="Upload Gagal";
        }
    }else{
        $kode=0;
        $pesan="Request Error";
    }
}else{
    $kode=0;
    $pesan="Request Invalid";
}

$res['kode']=$kode;
$res['pesan']=$pesan;

echo json_encode($res);
?>