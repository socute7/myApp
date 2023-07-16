package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InvoiceProdukActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    InvoiceProdukAdapter adapter;
    List<Barang> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_produk);
        int id = getIntent().getIntExtra("id", 0);

        recyclerView = findViewById(R.id.rv_invoice_produk);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InvoiceProdukAdapter(list, InvoiceProdukActivity.this);
        recyclerView.setAdapter(adapter);

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

                            adapter = new InvoiceProdukAdapter(list, InvoiceProdukActivity.this);
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