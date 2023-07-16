package com.example.UAS;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.MyViewHolder> {
    private final List<Barang> list;
    private final Activity activity;

    public ProdukAdapter(List<Barang> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_produk, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AndroidNetworking.initialize(activity.getApplicationContext());
        Barang barang = list.get(position);
        holder.tv_nama.setText(barang.nama);
        holder.tv_harga.setText("Rp. " + barang.harga);
        holder.tv_stok.setText("Stok " + barang.harga);
        Glide.with(holder.itemView)
                .load(list.get(position).getImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .centerCrop()
                .into(holder.gambar);

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.get(ServerAPI.PRODUK_DESTROY + "?id=" + barang.id)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int value = response.getInt("value");
//                                    if (value == 1) {
//                                        Toast.makeText(activity.getApplicationContext(), "Delete berhasil!", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(activity.getApplicationContext(), ProdukActivity.class);
//                                        activity.startActivity(intent);
//                                    } else {
//                                        Toast.makeText(activity.getApplicationContext(), "Delete gagal!", Toast.LENGTH_LONG).show();
//                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("anError", anError.getMessage());
                                Toast.makeText(activity.getApplicationContext(), "Delete gagal!", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        TextView tv_nama, tv_harga, tv_stok;
        TextView btn_delete, btn_edit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gambar = itemView.findViewById(R.id.gambar);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_harga = itemView.findViewById(R.id.tv_harga);
            tv_stok = itemView.findViewById(R.id.tv_stok);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}