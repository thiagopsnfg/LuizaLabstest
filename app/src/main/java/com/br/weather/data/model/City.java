package com.br.weather.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class City {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("coord")
    private Coord coord;

    @SerializedName("main")
    private Temperature temperature;

    @SerializedName("weather")
    private List<Weather> weatherList = new ArrayList<>();

    private float distanceFromMe;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public float getDistanceFromMe() {
        return distanceFromMe;
    }

    public void setDistanceFromMe(float distanceFromMe) {
        this.distanceFromMe = distanceFromMe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;

        City city = (City) o;

        if (getId() != city.getId()) return false;
        if (getName() != null ? !getName().equals(city.getName()) : city.getName() != null)
            return false;
        if (getCoord() != null ? !getCoord().equals(city.getCoord()) : city.getCoord() != null)
            return false;
        if (getTemperature() != null ? !getTemperature().equals(city.getTemperature()) : city.getTemperature() != null)
            return false;
        return getWeatherList() != null ? getWeatherList().equals(city.getWeatherList()) : city.getWeatherList() == null;

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getCoord() != null ? getCoord().hashCode() : 0);
        result = 31 * result + (getTemperature() != null ? getTemperature().hashCode() : 0);
        result = 31 * result + (getWeatherList() != null ? getWeatherList().hashCode() : 0);
        return result;
    }
}
