package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PembayaranActivity extends AppCompatActivity {
    TextView tv_status, tv_tanggal, tv_invoice, tv_tujuan, tv_alamat, tv_nama, tv_no_hp, tv_harga_total, tv_harga_ongkir, tv_total;

    Button btn_cetak, btn_gallery, btn_kirim, btn_home;
    ImageView iv_gambar;

    Uri selectedImageUri;
    List<Barang> list = new ArrayList<>();
    int harga_total, berat_total, total, ongkir = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);
        AndroidNetworking.initialize(getApplicationContext());

        tv_invoice = findViewById(R.id.tv_invoice);
        tv_tujuan = findViewById(R.id.tv_tujuan);
        tv_alamat = findViewById(R.id.tv_alamat);
        tv_nama = findViewById(R.id.tv_nama);
        tv_no_hp = findViewById(R.id.tv_no_hp);
        tv_harga_total = findViewById(R.id.tv_harga_total);
        tv_harga_ongkir = findViewById(R.id.tv_harga_ongkir);
        tv_total = findViewById(R.id.tv_total);
        btn_home = findViewById(R.id.btn_home);

        btn_cetak.setVisibility(View.GONE);
        btn_home.setVisibility(View.GONE);
        int id = getIntent().getIntExtra("id", 0);

        AndroidNetworking.get(ServerAPI.INVOICE_GET + "?id=" + id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            int status = data.getInt("status");
                            if (status == 0) {
                                tv_status.setText("Belum Bayar");
                            } else if (status == 1) {
                                tv_status.setText("Sudah Bayar");
                            }
                            ongkir = data.getInt("harga_ongkir");
                            harga_total = data.getInt("harga_total");
                            berat_total = data.getInt("berat_total");
                            total = harga_total + ongkir;

                            tv_tanggal.setText(data.getString("tanggal"));
                            tv_invoice.setText(data.getString("invoice"));
                            tv_tujuan.setText(data.getString("tujuan"));
                            tv_alamat.setText(data.getString("alamat"));
                            tv_nama.setText(data.getString("nama"));
                            tv_no_hp.setText(data.getString("no_hp"));
                            tv_harga_total.setText("Rp. " + data.getString("harga_total"));
                            tv_harga_ongkir.setText("Rp. " + data.getString("harga_ongkir"));
                            tv_total.setText("Rp. " + total);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("get data", anError.getMessage());
                    }
                });

        AndroidNetworking.get(ServerAPI.INVOICE_GET_PRODUK + "?id=" + id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject produk = data.getJSONObject(i);
                                list.add(new Barang(
                                        produk.getInt("id"),
                                        produk.getString("nama"),
                                        produk.getString("deskripsi"),
                                        produk.getInt("harga"),
                                        produk.getInt("berat"),
                                        produk.getInt("stok"),
                                        produk.getString("gambar"),
                                        "",
                                        0
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("get data", anError.getMessage());
                    }
                });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), 200);
            }
        });

        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImageUri == null) {
                    Toast.makeText(getApplicationContext(), "Upload bukti pembayaran dulu!", Toast.LENGTH_LONG).show();
                } else {
                    AndroidNetworking.post(ServerAPI.INVOICE_BAYAR + "?id=" + id)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        int value = response.getInt("value");
                                        if (value == 1) {
                                            Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_LONG).show();
                                            btn_gallery.setVisibility(View.GONE);
                                            btn_kirim.setVisibility(View.GONE);
                                            btn_cetak.setVisibility(View.VISIBLE);
                                            btn_home.setVisibility(View.VISIBLE);
                                            tv_status.setText("Sudah Bayar");
                                            tv_status.setTextColor(Color.GREEN);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d("anError", anError.getMessage());
                                    Toast.makeText(getApplicationContext(), "Gagal!", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        btn_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d("pdf", e.getMessage());
                }
            }
        });

//        btn_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(PembayaranActivity.this, DashboardActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                iv_gambar.setImageURI(selectedImageUri);
            }
        }
    }

    private void createPDF() throws FileNotFoundException {
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String format = s.format(new Date());
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, format + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        document.add(new Paragraph("E-Sambongrejo").setFontSize(20f).setBold().setFontColor(new DeviceRgb(66, 133, 244)));
        document.add(new com.itextpdf.layout.element.List().add("Semarang"));
        document.add(new Paragraph(""));
        document.add(new Paragraph("BILLED TO").setBold().setFontColor(new DeviceRgb(66, 133, 244)));
        document.add(new com.itextpdf.layout.element.List()
                .add(tv_nama.getText().toString())
                .add(tv_tujuan.getText().toString())
                .add(tv_alamat.getText().toString())
                .add(tv_no_hp.getText().toString())
        );
        document.add(new Paragraph(""));
        document.add(new Paragraph("INVOICE").setBold().setFontColor(new DeviceRgb(66, 133, 244)));
        document.add(new Paragraph("Invoice \n" + tv_invoice.getText().toString()));
        document.add(new Paragraph("Date of Issue \n" + tv_tanggal.getText().toString()));

        float[] columnwidth = {200, 120, 120};
        Table table = new Table(columnwidth);

        table.addCell(new Cell().add(new Paragraph("Produk").setBold()));
        table.addCell(new Cell().add(new Paragraph("Berat (gram)").setBold()));
        table.addCell(new Cell().add(new Paragraph("Harga").setBold()));

        for (Barang barang : list) {
            table.addCell(new Cell().add(new Paragraph(barang.nama)));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(barang.berat))));
            table.addCell(new Cell().add(new Paragraph("Rp. " + barang.harga)));
        }
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Subtotal").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(list.size())).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Discount").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("0").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Total Harga Barang").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Rp. " + harga_total).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Total Berat (gram)").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(berat_total)).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Ongkir").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Rp. " + ongkir).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell(1, 2).add(new Paragraph("Total Bayar \n Rp. " + total).setFontSize(14f).setBold().setTextAlignment(TextAlignment.CENTER)).setBorder(Border.NO_BORDER));
        document.add(table);

        document.close();
        Toast.makeText(this, "Downloaded", Toast.LENGTH_LONG).show();
    }
}