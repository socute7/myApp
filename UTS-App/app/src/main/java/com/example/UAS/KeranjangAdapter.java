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

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.MyViewHolder> {
    private final List<Keranjang> list;
    private final Activity activity;
    private ItemDeleteListener deleteListener;

    public interface ItemDeleteListener {
        void onDelete();
    }

    public void setDeleteListener(ItemDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }
    public KeranjangAdapter(List<Keranjang> list, Activity activity) {
        this.list = list;
        this.activity = activity;

        AndroidNetworking.initialize(activity.getApplicationContext());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_keranjang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Keranjang keranjang = list.get(position);
        holder.tv_nama.setText(keranjang.nama);
        holder.tv_harga.setText("Rp. " + keranjang.harga);
        holder.tv_berat.setText(keranjang.berat + " gram");
        holder.tv_qty.setText("Total : " + keranjang.qty);
        Glide.with(holder.itemView)
                .load(list.get(position).getGambar())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .centerCrop()
                .into(holder.gambar);
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.get(ServerAPI.KERANJANG_DESTROY + "?id=" + keranjang.id)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int value = response.getInt("value");
                                    if (value == 1){
                                        Toast.makeText(activity.getApplicationContext(), "Delete berhasil!", Toast.LENGTH_LONG).show();
                                        if(deleteListener != null){
                                            deleteListener.onDelete();
                                        }
                                    } else {
                                        Toast.makeText(activity.getApplicationContext(), "Delete gagal!", Toast.LENGTH_LONG).show();
                                    }
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
        ImageView gambar, iv_delete;
        TextView tv_nama, tv_harga, tv_berat, tv_qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gambar = itemView.findViewById(R.id.gambar);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_harga = itemView.findViewById(R.id.tv_harga);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            tv_berat = itemView.findViewById(R.id.tv_berat);
            tv_qty = itemView.findViewById(R.id.tv_qty);
        }
    }
}
