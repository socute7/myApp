package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.UAS.R;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateInvoiceActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText et_alamat, et_nama, et_no_hp, et_berat_total, et_harga_total, et_kurir, et_tujuan, et_harga_ongkir;
    Button btn_simpan, btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        AndroidNetworking.initialize(this);
        sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        et_alamat = findViewById(R.id.et_alamat);
        et_nama = findViewById(R.id.et_nama);
        et_no_hp = findViewById(R.id.et_no_hp);
        et_berat_total = findViewById(R.id.et_berat_total);
        et_harga_total = findViewById(R.id.et_harga_total);
        et_kurir = findViewById(R.id.et_kurir);
        et_tujuan = findViewById(R.id.et_tujuan);
        et_harga_ongkir = findViewById(R.id.et_harga_ongkir);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_back = findViewById(R.id.btn_back);

        et_nama.setText(sharedPreferences.getString("nama",""));
        et_alamat.setText(sharedPreferences.getString("alamat",""));
        et_tujuan.setText(getIntent().getStringExtra("tujuan"));
        et_kurir.setText(getIntent().getStringExtra("kurir"));
        et_berat_total.setText(String.valueOf(getIntent().getIntExtra("berat_total", 0)) + " gram");
        et_harga_ongkir.setText("Rp. " + String.valueOf(getIntent().getIntExtra("harga_ongkir", 0)));
        et_harga_total.setText("Rp. " + String.valueOf(getIntent().getIntExtra("harga_total", 0)));

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post(ServerAPI.INVOICE_STORE)
                        .addBodyParameter("tujuan", getIntent().getStringExtra("tujuan"))
                        .addBodyParameter("alamat", et_alamat.getText().toString())
                        .addBodyParameter("nama", et_nama.getText().toString())
                        .addBodyParameter("no_hp", et_no_hp.getText().toString())
                        .addBodyParameter("berat_total", String.valueOf(getIntent().getIntExtra("berat_total", 0)))
                        .addBodyParameter("harga_total", String.valueOf(getIntent().getIntExtra("harga_total", 0)))
                        .addBodyParameter("kurir", et_kurir.getText().toString())
                        .addBodyParameter("harga_ongkir", String.valueOf(getIntent().getIntExtra("harga_ongkir", 0)))
                        .addBodyParameter("user_id", String.valueOf(sharedPreferences.getInt("id", 0)))
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int value = response.getInt("value");
                                    if (value == 1){
                                        Toast.makeText(getApplicationContext(), "Terimakasih Sudah Berbelanja!", Toast.LENGTH_SHORT).show();
                                        int id = response.getInt("id");
                                        Intent intent = new Intent(CreateInvoiceActivity.this, HomeActivity.class);
                                        intent.putExtra("key", "Cart");
                                        intent.putExtra("action","success");
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Gagal membuat invoice!", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getApplicationContext(), "Gagal membuat invoice!", Toast.LENGTH_LONG).show();
                                Log.d("anError", anError.getMessage());
                            }
                        });
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateInvoiceActivity.this, HomeActivity.class);
                intent.putExtra("key", "Cart");
                intent.putExtra("action","back");
                startActivity(intent);
            }
        });
    }
}