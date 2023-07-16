package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PembelianActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    PembelianAdapter adapter;
    List<Pembelian> list;
    Button btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian);
        AndroidNetworking.initialize(getApplicationContext());

        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        list = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_pembelian);
        btn_home = findViewById(R.id.btn_home);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PembelianAdapter(list, PembelianActivity.this);
        recyclerView.setAdapter(adapter);

//        btn_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(PembelianActivity.this, DashboardActivity.class);
//                startActivity(intent);
//            }
//        });

        AndroidNetworking.get(ServerAPI.PEMBELIAN_GET + "?user_id=" + sharedPreferences.getInt("id", 0))
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject produk = data.getJSONObject(i);
                                list.add(new Pembelian(
                                        produk.getInt("id"),
                                        produk.getString("invoice"),
                                        produk.getInt("status"),
                                        produk.getString("tanggal")
                                ));
                            }

                            adapter = new PembelianAdapter(list, PembelianActivity.this);
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