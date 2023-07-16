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

public class KonsumenFormActivity extends AppCompatActivity {
    EditText nama, username, password, umur, alamat;
    Button btn_simpan, btn_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsumen_form);
        AndroidNetworking.initialize(getApplicationContext());

        nama = findViewById(R.id.nama);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        umur = findViewById(R.id.umur);
        alamat = findViewById(R.id.alamat);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_reset = findViewById(R.id.btn_reset);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nama.getText().toString().equals("") || username.getText().toString().equals("") || password.getText().toString().equals("") || umur.getText().toString().equals("") || alamat.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Semua data harus diisi!", Toast.LENGTH_LONG).show();
                } else {
                    store();
                }
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama.setText("");
                username.setText("");
                password.setText("");
                umur.setText("");
                alamat.setText("");
            }
        });
    }

    private void store() {
        AndroidNetworking.post(ServerAPI.REGISTRASI)
                .addBodyParameter("nama", nama.getText().toString())
                .addBodyParameter("username", username.getText().toString())
                .addBodyParameter("password", password.getText().toString())
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
                                nama.setText("");
                                username.setText("");
                                password.setText("");
                                umur.setText("");
                                alamat.setText("");
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