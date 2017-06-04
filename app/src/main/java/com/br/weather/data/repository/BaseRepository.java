package com.br.weather.data.repository;


import android.content.Context;

import com.br.weather.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRepository {

    private final String mApiPath;
    final String apiKey;

    private final Gson gson = new GsonBuilder().create();

    public BaseRepository(Context context) {
        this.mApiPath = context.getString(R.string.api_url);
        this.apiKey = context.getString(R.string.api_key);
    }

    Retrofit getRestApi() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mApiPath)
                .build();
    }
}
