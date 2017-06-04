package com.br.weather.data.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.br.weather.R;
import com.br.weather.data.model.City;
import com.br.weather.data.model.Climates;
import com.br.weather.data.service.WeatherService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherRepository extends BaseRepository {

    private Context mContext;

    public WeatherRepository(Context context) {
        super(context);
        this.mContext = context;
    }

    public void getWeathers(String latlngBounds, final Message message) {

        getRestApi().create(WeatherService.class)
                .getClimates(
                        latlngBounds,
                        mContext.getString(R.string.temp_unit),
                        mContext.getString(R.string.temp_lang),
                        apiKey)
                .enqueue(new Callback<Climates>() {
                    @Override
                    public void onResponse(@NonNull Call<Climates> call, @NonNull Response<Climates> response) {
                        //tratar resposta quando a nulidade
                        //Persistir dados
                        //retornar.
                        message.onSuccess(response.body().getCities());
                    }

                    @Override
                    public void onFailure(Call<Climates> call, Throwable t) {
                        message.onError();
                    }
                });
    }


    public interface Message {
        void onSuccess(List<City> cities);

        void onError();
    }


}
