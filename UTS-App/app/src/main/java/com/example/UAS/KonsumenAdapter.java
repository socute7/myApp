package com.example.UAS;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class KonsumenAdapter extends RecyclerView.Adapter<KonsumenAdapter.MyViewHolder>{
    private final List<Konsumen> list;
    private final Activity activity;

    public KonsumenAdapter(List<Konsumen> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_konsumen, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AndroidNetworking.initialize(activity.getApplicationContext());
        Konsumen konsumen = list.get(position);
        holder.tv_id.setText(String.valueOf(konsumen.id));
        holder.tv_username.setText(konsumen.username);
        holder.tv_nama.setText(konsumen.nama);
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), KonsumenUpdateActivity.class);
                intent.putExtra("id", konsumen.id);
                activity.startActivity(intent);
            }
        });
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidNetworking.get(ServerAPI.USER_DESTROY + "?id=" + konsumen.id)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int value = response.getInt("value");
                                    if (value == 1){
                                        Toast.makeText(activity.getApplicationContext(), "Delete berhasil!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(activity.getApplicationContext(), KonsumenActivity.class);
                                        activity.startActivity(intent);
                                    } else {
                                        Toast.makeText(activity.getApplicationContext(), "Delete gagal!", Toast.LENGTH_LONG).show(); }
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
        TextView tv_id, tv_username, tv_nama;
        TextView btn_edit, btn_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_id = itemView.findViewById(R.id.tv_id);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_nama = itemView.findViewById(R.id.tv_nama);
            btn_edit = itemView.findViewById(R.id.btn_edit);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
