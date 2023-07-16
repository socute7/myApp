package com.example.UAS;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardFragment extends Fragment {

    SharedPreferences mPreferences;
    BarangAdapter barangAdapter;
    List<Barang> list;
    List<Barang> filteredList;
    List<SlideModel> listImage;
    RecyclerView recyclerView;

    SearchView searchView;

    ImageView kategori1,kategori3, kategori2, kategori4;

    ImageSlider imageSlider;
    int total = 0;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidNetworking.initialize(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mPreferences = requireContext().getSharedPreferences("user", MODE_PRIVATE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        imageSlider = (ImageSlider) view.findViewById(R.id.imageSlider);
        searchView = (SearchView) view.findViewById(R.id.searchBar);
        kategori1 = (ImageView) view.findViewById(R.id.kategori1);
        kategori2 = (ImageView) view.findViewById(R.id.kategori2);
        kategori3 = (ImageView) view.findViewById(R.id.kategori3);
        kategori4 = (ImageView) view.findViewById(R.id.kategori4);

        listImage = new ArrayList<>();

        listImage.add(new SlideModel(R.drawable.i1,  ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.i1, ScaleTypes.FIT));
        listImage.add(new SlideModel(R.drawable.i1, ScaleTypes.FIT));

        imageSlider.setImageList(listImage);

        list = new ArrayList<>();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        barangAdapter = new BarangAdapter(list, getActivity());
        recyclerView.setAdapter(barangAdapter);

        AndroidNetworking.get(ServerAPI.GET_BARANG_ALL)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject produk = data.getJSONObject(i);
                                list.add(new Barang(
                                        produk.getInt("id"),
                                        produk.getString("nama"),
                                        produk.getString("deskripsi"),
                                        produk.getInt("harga"),
                                        produk.getInt("berat"),
                                        produk.getInt("stok"),
                                        produk.getString("gambar"),
                                        produk.getString("kategori"),
                                        0
                                ));
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                filteredList = list.stream()
                                        .filter(x -> x.nama.toLowerCase().contains("reel"))
                                        .collect(Collectors.toList());

                                if (filteredList.isEmpty()) {
                                    Toast t = Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT);
                                    t.show();
                                }
                                barangAdapter = new BarangAdapter(filteredList, getActivity());
                                recyclerView.setAdapter(barangAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("get data", anError.getMessage());
                    }
                });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    filteredList = list.stream()
                            .filter(x -> x.nama.toLowerCase().contains(query.toLowerCase()))
                            .collect(Collectors.toList());

                    if(filteredList.isEmpty()){
                        Toast t = Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    barangAdapter = new BarangAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(barangAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        kategori1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    filteredList = list.stream()
                            .filter(x -> x.nama.toLowerCase().contains("reel"))
                            .collect(Collectors.toList());

                    if(filteredList.isEmpty()){
                        Toast t = Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    barangAdapter = new BarangAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(barangAdapter);
                }
            }
        });

        kategori2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    filteredList = list.stream()
                            .filter(x -> x.nama.toLowerCase().contains("joran"))
                            .collect(Collectors.toList());

                    if(filteredList.isEmpty()){
                        Toast t = Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    barangAdapter = new BarangAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(barangAdapter);
                }
            }
        });

        kategori3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    filteredList = list.stream()
                            .filter(x -> x.nama.toLowerCase().contains("pelampung"))
                            .collect(Collectors.toList());

                    if(filteredList.isEmpty()){
                        Toast t = Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    barangAdapter = new BarangAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(barangAdapter);
                }
            }
        });

        kategori4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    filteredList = list.stream()
                            .filter(x -> x.nama.toLowerCase().contains("senar"))
                            .collect(Collectors.toList());

                    if(filteredList.isEmpty()){
                        Toast t = Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    barangAdapter = new BarangAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(barangAdapter);
                }
            }
        });

        searchView.findViewById(R.id.search_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                barangAdapter = new BarangAdapter(list, getActivity());
                recyclerView.setAdapter(barangAdapter);
            }
        });
        return view;
    }
}