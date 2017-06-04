package com.br.weather.utils;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.Locale;


public class WeatherUtils {

    public static final int CELSIUS = 0;
    public static final int FAHRENHEIT = 1;
    private static final int AREA_TO_SEARCH = 50000;

    public static int currentTemperatureUnit = CELSIUS;

    public static String converterCelsiusToFahrenheit(double temperature) {
        if (currentTemperatureUnit == CELSIUS) {
            return String.valueOf((int) temperature);
        }

        return String.valueOf((int) temperature * 9 / 5 + 32);
    }

    public static String getSearchArea(@NonNull final LatLng location) {

        LatLng norhtEast = SphericalUtil.computeOffset(location, AREA_TO_SEARCH * Math.sqrt(2), 315);
        LatLng southWest = SphericalUtil.computeOffset(location, AREA_TO_SEARCH * Math.sqrt(2), 135);

        return String
                .format(Locale.getDefault(), "%f?%f?%f?%f?100", southWest.longitude, southWest.latitude, norhtEast.longitude, norhtEast.latitude)
                .replace(",", ".")
                .replace("?", ",");

    }
}
