package com.br.weather.view.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.br.weather.R;
import com.br.weather.data.model.City;
import com.br.weather.utils.ImageUtils;
import com.br.weather.utils.WeatherUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Locale;

public class WeatherMapAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;

    public WeatherMapAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        City city = (City) marker.getTag();

        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_marker_content, null);

        TextView markeTemp = (TextView) view.findViewById(R.id.marke_temp);
        ImageView markeIcon = (ImageView) view.findViewById(R.id.marke_icon);

        markeTemp.setText(String.format(mContext.getString(R.string.graus),
                WeatherUtils.converterCelsiusToFahrenheit(city.getTemperature().getTemp())));

        ImageUtils.loadImage(markeIcon,
                String.format(Locale.getDefault(), mContext.getString(R.string.api_icon_url), city.getWeatherList().get(0).getIcon()),
                30, 30,
                R.drawable.ic_happy_cloud,
                true, true);


        return view;
    }
}
