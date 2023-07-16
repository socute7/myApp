package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class LaporanPenjualanActivity extends AppCompatActivity {
    Button btn_download;
    RecyclerView recyclerView;
    LaporanPenjualanAdapter adapter;
    List<LaporanPenjualan> list;
    int total = 0;
    int periodik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_penjualan);

        btn_download = findViewById(R.id.btn_download);
        list = new ArrayList<>();
        periodik = getIntent().getIntExtra("periodik", 0);

        recyclerView = findViewById(R.id.rv_laporan_penjualan);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LaporanPenjualanAdapter(list, LaporanPenjualanActivity.this);
        recyclerView.setAdapter(adapter);

        btn_download.setOnClickListener(new View.OnClickListener() {
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

        if (periodik == 1) {
            String start = getIntent().getStringExtra("start");
            String end = getIntent().getStringExtra("end");
            AndroidNetworking.get(ServerAPI.INVOICE_GET_PERIODIK + "?start=" + start + "&end=" + end)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject produk = data.getJSONObject(i);
                                    total += produk.getInt("harga");
                                    list.add(new LaporanPenjualan(
                                            produk.getInt("id"),
                                            produk.getString("username"),
                                            produk.getString("nama"),
                                            produk.getInt("harga"),
                                            produk.getString("tanggal")
                                    ));
                                }

                                adapter = new LaporanPenjualanAdapter(list, LaporanPenjualanActivity.this);
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d("get data", anError.getMessage());
                        }
                    });
        } else {
            AndroidNetworking.get(ServerAPI.INVOICE_GET_ALL)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray data = response.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject produk = data.getJSONObject(i);
                                    total += produk.getInt("harga");
                                    list.add(new LaporanPenjualan(
                                            produk.getInt("id"),
                                            produk.getString("username"),
                                            produk.getString("nama"),
                                            produk.getInt("harga"),
                                            produk.getString("tanggal")
                                    ));
                                }

                                adapter = new LaporanPenjualanAdapter(list, LaporanPenjualanActivity.this);
                                recyclerView.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.d("get data", anError.getMessage());
                        }
                    });
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
        document.add(new Paragraph("Semua Penjualan"));

        float[] columnwidth = {20, 120, 200, 120, 120};
        Table table = new Table(columnwidth);

        table.addCell(new Cell().add(new Paragraph("No").setBold()));
        table.addCell(new Cell().add(new Paragraph("Username").setBold()));
        table.addCell(new Cell().add(new Paragraph("Nama Produk").setBold()));
        table.addCell(new Cell().add(new Paragraph("Harga").setBold()));
        table.addCell(new Cell().add(new Paragraph("Tanggal Beli").setBold()));

        for (int i = 0; i < list.size(); i++) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(i + 1))));
            table.addCell(new Cell().add(new Paragraph(list.get(i).username)));
            table.addCell(new Cell().add(new Paragraph(list.get(i).nama)));
            table.addCell(new Cell().add(new Paragraph("Rp. " + list.get(i).harga)));
            table.addCell(new Cell().add(new Paragraph(list.get(i).tanggal)));
        }

        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Jumlah").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(list.size())).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));


        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph()).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Total").setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph("Rp. " + total).setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER));

        document.add(table);

        document.close();
        Toast.makeText(this, "Downloaded", Toast.LENGTH_LONG).show();
    }
}