package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    TextView tv_nama, tv_deskripsi, tv_harga, tv_stok, tv_berat;
    ImageView iv_gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_nama = findViewById(R.id.tv_nama);
        tv_deskripsi = findViewById(R.id.tv_deskripsi);
        tv_harga = findViewById(R.id.tv_harga);
        tv_stok = findViewById(R.id.tv_stok);
        iv_gambar = findViewById(R.id.iv_gambar);
        tv_berat = findViewById(R.id.tv_berat);

        tv_nama.setText(getIntent().getStringExtra("nama"));
        tv_deskripsi.setText(getIntent().getStringExtra("deskripsi"));
        tv_harga.setText("Rp. "+ getIntent().getIntExtra("harga", 0));
        tv_stok.setText("Stok: "+ getIntent().getIntExtra("stok", 0));
        tv_stok.setText("Berat: "+ getIntent().getIntExtra("berat", 0) + " gram");
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("gambar"))
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(iv_gambar);
    }
}