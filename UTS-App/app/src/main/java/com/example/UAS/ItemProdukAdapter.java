package com.example.UAS;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemProdukAdapter extends RecyclerView.Adapter<ItemProdukAdapter.MyViewHolder> {
    private ItemClickListener clickListener;

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private final List<Barang> list;
    private Activity activity;

    public ItemProdukAdapter(List<Barang> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    public ItemProdukAdapter(List<Barang> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_produk_dashboard, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Barang barang = list.get(position);
        holder.tv_nama.setText(barang.nama);
        holder.tv_harga.setText("Rp. " + barang.harga);
        Glide.with(holder.itemView)
                .load(list.get(position).getImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .centerCrop()
                .into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView gambar;
        TextView tv_nama, tv_harga;

        ImageButton ibChart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gambar = itemView.findViewById(R.id.gambar);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_harga = itemView.findViewById(R.id.tv_harga);
            ibChart = itemView.findViewById(R.id.ib_cart);

            itemView.setOnClickListener(this);
            gambar.setOnClickListener(this);
            ibChart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null){
                clickListener.onClick(view, getAdapterPosition());
            }
        }
    }
}
