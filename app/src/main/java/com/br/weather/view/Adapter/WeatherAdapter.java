package com.br.weather.view.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.weather.R;
import com.br.weather.data.model.City;
import com.br.weather.utils.ImageUtils;
import com.br.weather.utils.TextUtils;
import com.br.weather.utils.WeatherUtils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<City> mcities;

    public WeatherAdapter(List<City> cities) {
        this.mcities = cities;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_weather_list_item, parent, false);
        return new WeatherViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((WeatherViewHolder) holder).bindData(mcities.get(position));
    }

    @Override
    public int getItemCount() {
        return mcities != null ? mcities.size() : 0;
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.temp_city) TextView tempCity;
        @BindView(R.id.temp_desc) TextView tempDesc;
        @BindView(R.id.temp_now) TextView tempNow;
        @BindView(R.id.temp_min) TextView tempMin;
        @BindView(R.id.temp_max) TextView tempMax;
        @BindView(R.id.temp_near) TextView tempNear;
        @BindView(R.id.temp_icon) ImageView tempIcon;
        private Context mContext;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        public void bindData(City city) {
            tempCity.setText(city.getName());

            tempDesc.setText(TextUtils.capitalize(city.getWeatherList().get(0).getDescription()));

            tempNow.setText(String.format(mContext.getString(R.string.graus),
                    WeatherUtils.converterCelsiusToFahrenheit(city.getTemperature().getTemp())));

            tempMin.setText(String.format(mContext.getString(R.string.temp_min),
                    WeatherUtils.converterCelsiusToFahrenheit(city.getTemperature().getTempMin())));

            tempMax.setText(String.format(mContext.getString(R.string.temp_max),
                    WeatherUtils.converterCelsiusToFahrenheit(city.getTemperature().getTempMax())));

            tempNear.setText(String.format(mContext.getString(R.string.distance_unit),
                    city.getDistanceFromMe()));

            ImageUtils.loadImage(tempIcon,
                    String.format(Locale.getDefault(), mContext.getString(R.string.api_icon_url), city.getWeatherList().get(0).getIcon()),
                    50, 50,
                    R.drawable.ic_happy_cloud,
                    true, true);
        }
    }
}
