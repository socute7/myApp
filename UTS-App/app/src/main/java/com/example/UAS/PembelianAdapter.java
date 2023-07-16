package com.example.UAS;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;

import java.util.List;

public class PembelianAdapter extends RecyclerView.Adapter<PembelianAdapter.MyViewHolder> {
    private final List<Pembelian> list;
    private final Activity activity;

    public PembelianAdapter(List<Pembelian> list, Activity activity) {
        this.list = list;
        this.activity = activity;

        AndroidNetworking.initialize(activity.getApplicationContext());
    }

    @NonNull
    @Override
    public PembelianAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_pembelian, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PembelianAdapter.MyViewHolder holder, int position) {
        Pembelian pembelian = list.get(position);
        holder.tv_invoice.setText(pembelian.invoice);
        holder.tv_tanggal.setText(pembelian.tanggal);
        int status = pembelian.status;
        if (status == 0) {
            holder.tv_status.setText("Belum Bayar");
        } else if (status == 1) {
            holder.tv_status.setText("Sudah Bayar");
        }
        int id = pembelian.id;
        holder.cv_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), InvoiceProdukActivity.class);
                intent.putExtra("id", id);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_invoice, tv_tanggal, tv_status;
        CardView cv_card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_invoice = itemView.findViewById(R.id.tv_invoice);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);
            tv_status = itemView.findViewById(R.id.tv_status);
            cv_card = itemView.findViewById(R.id.cv_card);
        }
    }
}
