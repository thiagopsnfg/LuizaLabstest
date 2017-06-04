package com.br.weather.data.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Climates {
    @SerializedName("cod")
    private String Code;

    @SerializedName("list")
    private List<City> cities = new ArrayList<>();

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Climates)) return false;

        Climates climates = (Climates) o;

        if (getCode() != null ? !getCode().equals(climates.getCode()) : climates.getCode() != null)
            return false;
        return getCities() != null ? getCities().equals(climates.getCities()) : climates.getCities() == null;

    }

    @Override
    public int hashCode() {
        int result = getCode() != null ? getCode().hashCode() : 0;
        result = 31 * result + (getCities() != null ? getCities().hashCode() : 0);
        return result;
    }
}
