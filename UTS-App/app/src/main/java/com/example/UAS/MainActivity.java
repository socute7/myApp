package com.example.UAS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int id = mPreferences.getInt("id", 0);
        int role = mPreferences.getInt("role", 0);
        Log.d("id", String.valueOf(id));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                if (id != 0){
                    if (role == 0){
                        intent = new Intent(MainActivity.this, HomeActivity.class);
                    }
                }
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}