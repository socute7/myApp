package com.example.UAS;

public class ServerAPI {
    public static String BASE_URL = "https://cisostore.my.id/online_shop/";

    public static String REGISTRASI = BASE_URL + "/user/store.php";
    public static String LOGIN = BASE_URL + "/login.php";
    public static String GET_BARANG_ALL = BASE_URL + "/produk/get_all.php";
    public static String KERANJANG_STORE = BASE_URL + "/keranjang/store.php";
    public static String KERANJANG_GET_ALL = BASE_URL + "/keranjang/get_all.php";
    public static String KERANJANG_DESTROY = BASE_URL + "/keranjang/destroy.php";
    public static String INVOICE_STORE = BASE_URL + "/invoice/store.php";
    public static String INVOICE_GET = BASE_URL + "/invoice/invoice.php";
    public static String PEMBELIAN_GET = BASE_URL + "/invoice/get.php";

    public static String USER_GET_BY_ID = BASE_URL + "/user/get.php";
    public static String USER_UPDATE = BASE_URL + "/user/update.php";
    public static String USER_PASSWORD = BASE_URL + "/user/password.php";
    public static String USER_GET_ALL = BASE_URL + "/user/get_all.php";
    public static String USER_DESTROY = BASE_URL + "/user/destroy.php";

    public static String INVOICE_BAYAR = BASE_URL + "/invoice/bukti_bayar.php";
    public static String INVOICE_GET_PRODUK = BASE_URL + "/invoice/get_produk.php";
    public static String INVOICE_GET_ALL = BASE_URL + "/invoice/get_all.php";
    public static String INVOICE_GET_PERIODIK = BASE_URL + "/invoice/get_periodik.php";

    public static String PRODUK_STORE = BASE_URL + "/produk/store.php";
    public static String PRODUK_DESTROY = BASE_URL + "/produk/destroy.php";
    public static String PRODUK_UPDATE = BASE_URL + "/produk/update.php";
    public static String PRODUK_GET_BY_ID = BASE_URL + "/produk/get.php";
}
