package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PenjualanCekActivity extends AppCompatActivity {
    Button btn_all, btn_periodik, btn_home;
    EditText start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan_cek);

        btn_all = findViewById(R.id.btn_all);
        btn_periodik = findViewById(R.id.btn_periodik);
        btn_home = findViewById(R.id.btn_home);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);

        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PenjualanCekActivity.this, LaporanPenjualanActivity.class);
                intent.putExtra("periodik", 0);
                startActivity(intent);
            }
        });

        btn_periodik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start.getText().toString().equals("") || end.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Start dan End harus diisi!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(PenjualanCekActivity.this, LaporanPenjualanActivity.class);
                    intent.putExtra("periodik", 1);
                    intent.putExtra("start", start.getText().toString());
                    intent.putExtra("end", end.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}