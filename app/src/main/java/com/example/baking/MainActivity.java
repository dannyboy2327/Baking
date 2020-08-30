package com.example.baking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.baking.Adapters.RecipeAdapter;
import com.example.baking.Classes.Recipe;
import com.example.baking.Networks.JsonPlaceHolderApi;
import com.example.baking.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Retrofit mRetrofit;
    private JsonPlaceHolderApi mJsonPlaceHolderApi;
    private List<Recipe> mRecipes = new ArrayList<>();
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null) {
            mBinding.tvNoInternet.setVisibility(View.VISIBLE);
        } else if (activeNetworkInfo != null && savedInstanceState == null) {
            retrofitBuilder();
            jsonBuilder();
            recyclerViewBuild();
            getRecipes();
        } else if (activeNetworkInfo != null && savedInstanceState != null) {
            mRecipes = (List<Recipe>) savedInstanceState.getSerializable("recipes");
            recyclerViewBuild();
            setRecipes();
        }

    }

    private void retrofitBuilder() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void jsonBuilder() {
        mJsonPlaceHolderApi = mRetrofit.create(JsonPlaceHolderApi.class);
    }

    private void recyclerViewBuild() {
        if (!getResources().getBoolean(R.bool.isTablet)) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            mBinding.rvRecipes.setLayoutManager(linearLayoutManager);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            mBinding.rvRecipes.setLayoutManager(gridLayoutManager);
        }
        mBinding.rvRecipes.setHasFixedSize(true);
    }

    private void getRecipes() {
        Call<JsonArray> call = mJsonPlaceHolderApi.getBakingData();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d(LOG_TAG, "Call request working");

                        String listString = response.body().toString();

                        Type listType = new TypeToken<List<Recipe>>() {
                        }.getType();

                        mRecipes = getListFromJson(listString, listType);

                        setRecipes();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d(LOG_TAG, "Call request not working: " + t.getMessage());
            }
        });
    }

    private void setRecipes() {
        mBinding.rvRecipes.setAdapter(new RecipeAdapter(getApplicationContext(), mRecipes, new RecipeAdapter.RecipeViewClickListener() {
            @Override
            public void onRecipeClick(Recipe recipe) {
                SharedPreferences sharedPreferences = getSharedPreferences("recipe", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                Gson gson= new Gson();
                String json = gson.toJson(recipe);

                editor.putString("recipe", json);
                editor.apply();

                Intent detailsIntent = new Intent(MainActivity.this, RecipeDetails.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("details", recipe);
                detailsIntent.putExtras(bundle);
                startActivity(detailsIntent);
            }
        }));
    }

    private List<Recipe> getListFromJson(String listString, Type listType) {
        if (!isValid(listString)) {
            return null;
        }
        return new Gson().fromJson(listString, listType);
    }

    private boolean isValid(String listString) {
        try {
            new JsonParser().parse(listString);
            return true;
        } catch (JsonSyntaxException e) {
            return false;
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("recipes", (Serializable) mRecipes);
    }
}