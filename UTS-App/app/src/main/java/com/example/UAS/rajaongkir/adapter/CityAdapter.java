package com.example.UAS.rajaongkir.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.UAS.R;
import com.example.UAS.rajaongkir.model.city.Result;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends BaseAdapter {
    private final Activity activity;
    private LayoutInflater inflater;
    private final List<Result> movieItems;
    private final ArrayList<Result> listlokasiasli;

    public CityAdapter(Activity activity, List<Result> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;

        listlokasiasli = new ArrayList<Result>();
        listlokasiasli.addAll(movieItems);
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_category, null);

        TextView tv_category = (TextView) convertView.findViewById(R.id.tv_category);
        TextView tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);

        Result m = movieItems.get(position);

        tv_category.setText(m.getCityName());
        tv_detail.setText(m.getCityId());

        return convertView;
    }

    @SuppressLint("DefaultLocale")
    public void filter(String charText)
    {
        charText = charText.toLowerCase();

        movieItems.clear();
        if (charText.length() == 0) {
            /* tampilkan seluruh data */
            movieItems.addAll(listlokasiasli);

        } else {
            for (Result lok : listlokasiasli) {
                if (lok.getCityName().toLowerCase().contains(charText)) {
                    movieItems.add(lok);
                } else {
                }

            }
        }

        notifyDataSetChanged();
    }

    public void setList(List<Result> movieItems){
        this.listlokasiasli.addAll(movieItems);
    }
}
