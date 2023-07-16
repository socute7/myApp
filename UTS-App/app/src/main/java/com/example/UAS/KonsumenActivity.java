package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

public class KonsumenActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    KonsumenAdapter adapter;
    List<Konsumen> list;
    Button btn_create, btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsumen);
        AndroidNetworking.initialize(getApplicationContext());

        recyclerView = findViewById(R.id.rv_konsumen);
        btn_create = findViewById(R.id.btn_create);
        btn_home = findViewById(R.id.btn_home);

        list = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new KonsumenAdapter(list, KonsumenActivity.this);
        recyclerView.setAdapter(adapter);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KonsumenActivity.this, KonsumenFormActivity.class);
                startActivity(intent);
            }
        });

        AndroidNetworking.get(ServerAPI.USER_GET_ALL)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject produk = data.getJSONObject(i);
                                list.add(new Konsumen(
                                        produk.getInt("id"),
                                        produk.getString("nama"),
                                        produk.getString("username"),
                                        produk.getInt("umur"),
                                        produk.getString("alamat")
                                ));
                            }

                            adapter = new KonsumenAdapter(list, KonsumenActivity.this);
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