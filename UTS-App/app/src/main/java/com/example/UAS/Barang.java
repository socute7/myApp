package com.example.UAS;

public class Barang {
    public int id;
    public String nama;
    public String deskripsi;
    public int harga;
    public int berat;
    public int stok;
    public String gambar;
    public String kategori;

    public int qty;

    public Barang(int id, String nama, String deskripsi, int harga, int berat, int stok, String gambar, String kategori, int qty) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.berat = berat;
        this.stok = stok;
        this.gambar = gambar;
        this.kategori = kategori;
        this.qty = qty;
    }

    public String getImage(){
        return ServerAPI.BASE_URL + "/gambar/" + gambar;
    }
}
