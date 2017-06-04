package com.br.weather.data.model;


import com.google.gson.annotations.SerializedName;

public class Temperature {

    @SerializedName("temp")
    private double temp;

    @SerializedName("temp_min")
    private double tempMin;

    @SerializedName("temp_max")
    private double tempMax;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Temperature)) return false;

        Temperature that = (Temperature) o;

        if (Double.compare(that.getTemp(), getTemp()) != 0) return false;
        if (Double.compare(that.getTempMin(), getTempMin()) != 0) return false;
        return Double.compare(that.getTempMax(), getTempMax()) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp1;
        temp1 = Double.doubleToLongBits(getTemp());
        result = (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(getTempMin());
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        temp1 = Double.doubleToLongBits(getTempMax());
        result = 31 * result + (int) (temp1 ^ (temp1 >>> 32));
        return result;
    }
}
