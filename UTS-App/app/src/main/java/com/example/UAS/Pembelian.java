package com.example.UAS;

public class Pembelian {
    int id;
    String invoice;
    int status;
    String tanggal;

    public Pembelian(int id, String invoice, int status, String tanggal) {
        this.id = id;
        this.invoice = invoice;
        this.status = status;
        this.tanggal = tanggal;
    }
}
