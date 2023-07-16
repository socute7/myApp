package com.example.UAS;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;


import java.util.ArrayList;

public class ImageSliderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanState) {

        super.onCreate(savedInstanState);
        setContentView(R.layout.activity_dashboard);

        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModel = new ArrayList<>();

        slideModel.add(new SlideModel(R.drawable.i1, ScaleTypes.FIT));

        imageSlider.setImageList(slideModel,ScaleTypes.FIT);
    }
}
