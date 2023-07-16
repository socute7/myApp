package com.example.UAS;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;

public class SearchActivity extends AppCompatActivity {
    private static final String BASE_URL = "http://cisostore.my.id/online_shop/";//replace this wih the ip address for your computer
    private static final String FULL_URL = BASE_URL+"/PHP/spaceship/";
    class Spacecraft {
        @SerializedName("id")
        private int id;
        @SerializedName("nama")
        private String nama;
        @SerializedName("deskripsi")
        private String deskripsi;
        @SerializedName("harga")
        private String harga;
        @SerializedName("berat")
        private String berat;
        @SerializedName("stok")
        private String stok;
        @SerializedName("gambar")
        private String gambar;

        public Spacecraft(String nama){
            this.nama=nama;
        }
        public Spacecraft(int id, String nama, String deskripsi,String harga,String berat,String stok, String gambar) {
            this.id = id;
            this.nama = nama;
            this.deskripsi = deskripsi;
            this.harga = harga;
            this.berat = berat;
            this.stok = stok;
            this.gambar = gambar;
        }

        /*
         *GETTERS AND SETTERS
         */
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getNama() {
            return nama;
        }
        public String getDeskripsi() {
            return deskripsi;
        }
        public String getHarga() {
            return harga;
        }
        public String getBerat() {
            return berat;
        }
        public String getStok() {
            return stok;
        }
        public String getGambar() {
            return gambar;
        }
        @Override
        public String toString() {
            return nama;
        }
    }

    interface MyAPIService {

        @GET("/PHP/spaceship")
        Call<List<Spacecraft>> getSpacecrafts();
        @FormUrlEncoded
        @POST("/PHP/spaceship/index.php")
        Call<List<Spacecraft>> searchSpacecraft(@Field("query") String query);
    }

    static class RetrofitClientInstance {
        private static Retrofit retrofit;

        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }
    class ListViewAdapter extends BaseAdapter {

        private List<Spacecraft> spacecrafts;
        private Context context;

        public ListViewAdapter(Context context, List<Spacecraft> spacecrafts) {
            this.context = context;
            this.spacecrafts = spacecrafts;
        }

        @Override
        public int getCount() {
            return spacecrafts.size();
        }

        @Override
        public Object getItem(int pos) {
            return spacecrafts.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.model, viewGroup, false);
            }

            TextView nameTxt = view.findViewById(R.id.nameTextView);
            TextView txtPropellant = view.findViewById(R.id.propellantTextView);
            ImageView spacecraftImageView = view.findViewById(R.id.spacecraftImageView);

            final Spacecraft thisSpacecraft = spacecrafts.get(position);

            nameTxt.setText(thisSpacecraft.getNama());
            txtPropellant.setText(thisSpacecraft.getDeskripsi());

            if (thisSpacecraft.getGambar() != null && thisSpacecraft.getGambar().length() > 0) {
                Picasso.get().load(FULL_URL + "/gambar/" + thisSpacecraft.getGambar()).placeholder(R.drawable.logo).into(spacecraftImageView);
            } else {
                Toast.makeText(context, "Empty Image URL", Toast.LENGTH_LONG).show();
                Picasso.get().load(R.drawable.logo).into(spacecraftImageView);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, thisSpacecraft.getNama(), Toast.LENGTH_SHORT).show();
                    String[] spacecrafts = {
                            thisSpacecraft.getNama(),
                            thisSpacecraft.getDeskripsi(),
                            thisSpacecraft.getHarga(),
                            thisSpacecraft.getBerat(),
                            thisSpacecraft.getStok(),
                            FULL_URL + "/gambar/" + thisSpacecraft.getGambar()
                    };
                    openDetailActivity(spacecrafts);
                }
            });

            return view;
        }

        private void openDetailActivity(String[] data) {
            Intent intent = new Intent(SearchActivity.this, DetailSearchActivity.class);
            intent.putExtra("nama", data[0]);
            intent.putExtra("deskripsi", data[1]);
            intent.putExtra("harga", data[2]);
            intent.putExtra("berat", data[3]);
            intent.putExtra("stok", data[4]);
            intent.putExtra("gambar", data[5]);
            startActivity(intent);
        }
    }
    private ListViewAdapter adapter;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private SearchView mSearchView;

    private void initializeWidgets(){
//        mListView = findViewById(R.id.mListView);
//        mProgressBar= findViewById(R.id.mProgressBar);
//        mProgressBar.setIndeterminate(true);
//        mProgressBar.setVisibility(View.VISIBLE);
//        mSearchView=findViewById(R.id.mSearchView);
//        mSearchView.setIconified(true);
    }

    private void populateListView(List<Spacecraft> spacecraftList) {
        adapter = new ListViewAdapter(this,spacecraftList);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initializeWidgets();

        /*Create handle for the RetrofitInstance interface*/
        final MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);

        //Call<List<Spacecraft>> call = myAPIService.getSpacecrafts();
        final Call<List<Spacecraft>> call = myAPIService.searchSpacecraft("");
        call.enqueue(new Callback<List<Spacecraft>>() {

            @Override
            public void onResponse(Call<List<Spacecraft>> call, Response<List<Spacecraft>> response) {
                mProgressBar.setVisibility(View.GONE);
                populateListView(response.body());
            }
            @Override
            public void onFailure(Call<List<Spacecraft>> call, Throwable throwable) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                final Call<List<Spacecraft>> call = myAPIService.searchSpacecraft(query);
                call.enqueue(new Callback<List<Spacecraft>>() {

                    @Override
                    public void onResponse(Call<List<Spacecraft>> call, Response<List<Spacecraft>> response) {
                        mProgressBar.setVisibility(View.GONE);
                        populateListView(response.body());
                    }
                    @Override
                    public void onFailure(Call<List<Spacecraft>> call, Throwable throwable) {
                        populateListView(new ArrayList<Spacecraft>());
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(SearchActivity.this, "ERROR: "+throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
        });
    }
}
