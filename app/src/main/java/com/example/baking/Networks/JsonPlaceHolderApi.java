package com.example.baking.Networks;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("baking.json")
    Call<JsonArray> getBakingData();
}
