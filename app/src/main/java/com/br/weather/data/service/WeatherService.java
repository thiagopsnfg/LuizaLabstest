package com.br.weather.data.service;


import com.br.weather.data.model.Climates;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("box/city")
    Call<Climates> getClimates(@Query("bbox") String latlngBounds,
                               @Query("units") String TemperatureUnit,
                               @Query("lang") String lang,
                               @Query("APPID") String apiKey);
}
