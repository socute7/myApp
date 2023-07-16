package com.example.UAS;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.UAS.rajaongkir.PilihKurir;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements KeranjangAdapter.ItemDeleteListener {

    SharedPreferences sharedPreferences;
    TextView tv_total_harga, tv_total_berat;
    RecyclerView rv_keranjang;
    KeranjangAdapter keranjangAdapter;
    BottomNavigationView bottomBar;
    List<Keranjang> list;
    Button btn_checkout;
    int total_harga, total_berat = 0;

    public CartFragment() {
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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        sharedPreferences = requireContext().getSharedPreferences("user", MODE_PRIVATE);
        rv_keranjang = view.findViewById(R.id.rv_keranjang);
        list = new ArrayList<>();
        btn_checkout = view.findViewById(R.id.btn_checkout);
        tv_total_harga = view.findViewById(R.id.totalHarga);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 1);
        rv_keranjang.setLayoutManager(layoutManager);
        keranjangAdapter = new KeranjangAdapter(list, getActivity());
        rv_keranjang.setAdapter(keranjangAdapter);


        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_harga == 0){
                    Toast.makeText(requireContext(), "Keranjang Anda kosong!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(requireContext(), PilihKurir.class);
                    intent.putExtra("total_harga", total_harga);
                    intent.putExtra("total_berat", total_berat);
                    startActivity(intent);
                }
            }
        });

        getAllData();

        return view;
    }

    private void getAllData(){
        String jsonString = sharedPreferences.getString("listProduct", "");

        Gson g = new Gson();
        Cart cart = g.fromJson(jsonString, Cart.class);

        int idCart = 1;
        if(cart != null){
            if(cart.listProduct.size() > 0) {
                for (Barang data : cart.listProduct){
                    total_harga += data.harga;
                    total_berat += data.berat;
                    Integer qty = data.qty;
                    list.add(new Keranjang(
                            idCart,
                            data.id,
                            data.nama,
                            data.harga,
                            data.berat,
                            data.gambar,
                            qty
                    ));
                    idCart++;
                }

                tv_total_harga.setText("Rp. " + String.valueOf(total_harga));
                keranjangAdapter = new KeranjangAdapter(list, getActivity());
                keranjangAdapter.setDeleteListener(CartFragment.this);
                rv_keranjang.setAdapter(keranjangAdapter);
            }
        }

//        AndroidNetworking.get(ServerAPI.KERANJANG_GET_ALL + "?user_id=" + sharedPreferences.getInt("id", 0))
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            list.clear();
//                            total_harga = 0;
//                            total_berat = 0;
//                            JSONArray data = response.getJSONArray("data");
//                            for (int i = 0; i < data.length(); i++) {
//                                JSONObject produk = data.getJSONObject(i);
//                                total_harga += produk.getInt("harga");
//                                total_berat += produk.getInt("berat");
//                                Integer qty = produk.getInt("quantity");
//                                list.add(new Keranjang(
//                                        produk.getInt("id"),
//                                        produk.getInt("produk_id"),
//                                        produk.getString("nama"),
//                                        produk.getInt("harga"),
//                                        produk.getInt("berat"),
//                                        produk.getString("gambar"),
//                                        qty
//                                ));
//                            }
//
//                            tv_total_harga.setText("Rp. " + String.valueOf(total_harga));
//                            keranjangAdapter = new KeranjangAdapter(list, getActivity());
//                            keranjangAdapter.setDeleteListener(CartFragment.this);
//                            rv_keranjang.setAdapter(keranjangAdapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Log.d("get data", anError.getMessage());
//                    }
//                });
    }

    @Override
    public void onDelete() {
        getAllData();
    }
}