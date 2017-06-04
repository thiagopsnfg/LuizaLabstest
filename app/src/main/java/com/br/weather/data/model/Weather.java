package com.br.weather.data.model;

import com.google.gson.annotations.SerializedName;


public class Weather {

    @SerializedName("id")
    private int id;

    @SerializedName("main")
    private String main;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weather)) return false;

        Weather weather = (Weather) o;

        if (getId() != weather.getId()) return false;
        if (getMain() != null ? !getMain().equals(weather.getMain()) : weather.getMain() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(weather.getDescription()) : weather.getDescription() != null)
            return false;
        return getIcon() != null ? getIcon().equals(weather.getIcon()) : weather.getIcon() == null;

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + (getMain() != null ? getMain().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getIcon() != null ? getIcon().hashCode() : 0);
        return result;
    }
}
