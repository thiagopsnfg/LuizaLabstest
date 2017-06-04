package com.br.weather.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("Lat")
    private double lat;

    @SerializedName("Lon")
    private double lon;

    private LatLng latLng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lon);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coord)) return false;

        Coord coord = (Coord) o;

        if (Double.compare(coord.getLat(), getLat()) != 0) return false;
        return Double.compare(coord.getLon(), getLon()) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getLat());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLon());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }


}
