package com.example.UAS;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.denzcoskun.imageslider.ImageSlider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ProductFragment extends Fragment implements ItemProdukAdapter.ItemClickListener {

    SharedPreferences mPreferences;
    ItemProdukAdapter barangAdapter;
    List<Barang> list;
    List<Barang> filteredList;
    BottomNavigationView bottomBar;
    RecyclerView recyclerView;
    SearchView searchView;

    ImageSlider imageSlider;
    int total = 0;
    public ProductFragment() {
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
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        mPreferences = requireContext().getSharedPreferences("user", MODE_PRIVATE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        searchView = (SearchView) view.findViewById(R.id.searchBar);
        ProgressBar pgsBar = (ProgressBar) view.findViewById(R.id.pBarProduct);


        list = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        barangAdapter = new ItemProdukAdapter(list, getActivity());
        recyclerView.setAdapter(barangAdapter);

        pgsBar.setVisibility(View.VISIBLE);
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

                            pgsBar.setVisibility(View.GONE);
                            barangAdapter = new ItemProdukAdapter(list, getActivity());
                            barangAdapter.setClickListener(ProductFragment.this);
                            recyclerView.setAdapter(barangAdapter);
                        } catch (JSONException e) {
                            pgsBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pgsBar.setVisibility(View.GONE);
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
                        Toast t = Toast.makeText(getActivity(), "Data tidak ditemukan", Toast.LENGTH_SHORT);
                        t.show();
                    }
                    barangAdapter = new ItemProdukAdapter(filteredList, getActivity());
                    recyclerView.setAdapter(barangAdapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.findViewById(R.id.search_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", false);
                searchView.clearFocus();
                barangAdapter = new ItemProdukAdapter(list, getActivity());
                recyclerView.setAdapter(barangAdapter);
            }
        });
        return view;
    }
    @Override
    public void onClick(View view, int position) {
        Barang barang = list.get(position);

        switch (view.getId()) {
            case R.id.gambar: {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("id", barang.id);
                intent.putExtra("nama", barang.nama);
                intent.putExtra("deskripsi", barang.deskripsi);
                intent.putExtra("harga", barang.harga);
                intent.putExtra("berat", barang.berat);
                intent.putExtra("stok", barang.stok);
                intent.putExtra("gambar", barang.getImage());
                startActivity(intent);
                break;
            }
            case R.id.ib_cart: {
                saveProduct(barang);
//                AndroidNetworking.post(ServerAPI.KERANJANG_STORE)
//                        .addBodyParameter("user_id", String.valueOf(mPreferences.getInt("id", 0)))
//                        .addBodyParameter("produk_id", String.valueOf(barang.id))
//                        .setPriority(Priority.MEDIUM)
//                        .build()
//                        .getAsJSONObject(new JSONObjectRequestListener() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                try {
//                                    int value = response.getInt("value");
//                                    if (value == 1){
//                                        total += barang.harga;
//                                        Toast.makeText(requireContext(), "Tambah ke keranjang!", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(requireContext(), "Gagal tambah ke keranjang!", Toast.LENGTH_SHORT).show();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onError(ANError anError) {
//                                Toast.makeText(requireContext(), "Gagal tambah ke keranjang!", Toast.LENGTH_SHORT).show();
//                                Log.d("anError", anError.getMessage());
//                            }
//                        });
//                break;
            }
        }
    }

    private void saveProduct(Barang data){
        List<Barang> list = new ArrayList<>();
        String jsonString = mPreferences.getString("listProduct", "");

        Gson g = new Gson();
        Cart cart = g.fromJson(jsonString, Cart.class);
        Boolean isProductExists = false;

        if(cart != null){
            for(Barang item : cart.listProduct){
                if(data.id == item.id){
                    isProductExists = true;
                    item.harga += data.harga;
                    item.berat += data.berat;
                    item.qty++;
                }
            }
        } else {
            cart = new Cart(list);
        }

        if(isProductExists == false){
            cart.listProduct.add(
                    new Barang(
                            data.id,
                            data.nama,
                            data.deskripsi,
                            data.harga,
                            data.berat,
                            data.stok,
                            data.gambar,
                            data.kategori,
                            1
                    ));
        }

        String jsonProduct = g.toJson(cart);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString("listProduct", jsonProduct);

        editor.commit();
        Toast.makeText(requireContext(), "Barang berhasil ditambahkan ke keranjang!", Toast.LENGTH_SHORT).show();
    }
}