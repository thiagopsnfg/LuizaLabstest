package com.br.weather.utils;

import android.location.Location;

import com.br.weather.data.model.City;
import com.google.android.gms.maps.model.LatLng;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MapsUtils {

    public static List<City> sortCitiesNearMe(List<City> cities, final LatLng myLocation) {

        Comparator comp = (Comparator<City>) (c1, c2) -> {
            float[] result1 = new float[3];
            Location.distanceBetween(myLocation.latitude, myLocation.longitude, c1.getCoord().getLat(), c1.getCoord().getLon(), result1);
            Float distance1 = result1[0];

            float[] result2 = new float[3];
            Location.distanceBetween(myLocation.latitude, myLocation.longitude, c2.getCoord().getLat(), c2.getCoord().getLon(), result2);
            Float distance2 = result2[0];

            return distance1.compareTo(distance2);
        };


        Collections.sort(cities, comp);

        return distanceToMe(cities, myLocation);
    }

    private static List<City> distanceToMe(List<City> cities, LatLng myLocation) {

        for (City city : cities) {
            float[] result1 = new float[3];
            Location.distanceBetween(myLocation.latitude, myLocation.longitude, city.getCoord().getLat(), city.getCoord().getLon(), result1);
            Float distance = result1[0] / 1000;
            city.setDistanceFromMe(distance);
        }

        return cities;
    }

}
