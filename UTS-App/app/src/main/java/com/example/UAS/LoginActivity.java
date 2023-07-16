package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private final String mSharedPrefFile = "akun";
    EditText username, password;
    TextView tv_registrasi;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AndroidNetworking.initialize(getApplicationContext());

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        tv_registrasi = findViewById(R.id.tv_registrasi);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Semua data harus diisi!", Toast.LENGTH_LONG).show();
                } else {
                    login();
                }
            }
        });

        tv_registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrasiActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        AndroidNetworking.post(ServerAPI.LOGIN)
                .addBodyParameter("username", username.getText().toString())
                .addBodyParameter("password", password.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int value = response.getInt("value");
                            if (value == 1){
                                Toast.makeText(getApplicationContext(), "Login berhasil!", Toast.LENGTH_SHORT).show();
                                mPreferences = getSharedPreferences("user", MODE_PRIVATE);
                                SharedPreferences.Editor editor = mPreferences.edit();
                                editor.putInt("id", response.getInt("id"));
                                editor.putString("nama", response.getString("nama"));
                                editor.putString("username", response.getString("username"));
                                editor.putString("alamat", response.getString("alamat"));
                                editor.putInt("role", response.getInt("role"));
                                editor.commit();

                                if (response.getInt("role") == 0){
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Login gagal!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("anError", anError.getMessage());
                        Toast.makeText(getApplicationContext(), "Login gagal!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}