package com.example.UAS.rajaongkir.api;

import com.example.UAS.rajaongkir.model.city.ItemCity;
import com.example.UAS.rajaongkir.model.cost.ItemCost;
import com.example.UAS.rajaongkir.model.province.ItemProvince;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    // Province
    @GET("province")
    @Headers("key:71352f07418df0001cbe0a98386e1311")
    Call<ItemProvince> getProvince ();

    // City
    @GET("city")
    @Headers("key:71352f07418df0001cbe0a98386e1311")
    Call<ItemCity> getCity (@Query("province") String province);

    // Cost
    @FormUrlEncoded
    @POST("cost")
    Call<ItemCost> getCost (@Field("key") String Token,
                            @Field("origin") String origin,
                            @Field("destination") String destination,
                            @Field("weight") String weight,
                            @Field("courier") String courier);
}
