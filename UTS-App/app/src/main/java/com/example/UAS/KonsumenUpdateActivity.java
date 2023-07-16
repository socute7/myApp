package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

public class KonsumenUpdateActivity extends AppCompatActivity {
    EditText nama, username, umur, alamat, password;
    Button btn_simpan, btn_ganti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsumen_update);
        AndroidNetworking.initialize(getApplicationContext());

        int id = getIntent().getIntExtra("id", 0);

        nama = findViewById(R.id.nama);
        username = findViewById(R.id.username);
        umur = findViewById(R.id.umur);
        alamat = findViewById(R.id.alamat);
        password = findViewById(R.id.password);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_ganti = findViewById(R.id.btn_ganti);

        AndroidNetworking.get(ServerAPI.USER_GET_BY_ID + "?id=" + id)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            nama.setText(data.getString("nama"));
                            username.setText(data.getString("username"));
                            umur.setText(data.getString("umur"));
                            alamat.setText(data.getString("alamat"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("get data", anError.getMessage());
                    }
                });

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.post(ServerAPI.USER_UPDATE + "?id=" + id)
                        .addBodyParameter("nama", nama.getText().toString())
                        .addBodyParameter("username", username.getText().toString())
                        .addBodyParameter("umur", umur.getText().toString())
                        .addBodyParameter("alamat", alamat.getText().toString())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int value = response.getInt("value");
                                    if (value == 1){
                                        Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(), "Gagal simpan!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btn_ganti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Password harus diisi!", Toast.LENGTH_SHORT).show();
                } else {
                    AndroidNetworking.post(ServerAPI.USER_PASSWORD + "?id=" + id)
                            .addBodyParameter("password", password.getText().toString())
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        int value = response.getInt("value");
                                        if (value == 1){
                                            Toast.makeText(getApplicationContext(), "Berhasil!", Toast.LENGTH_LONG).show();
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
    }
}