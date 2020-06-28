package com.weather.shixun.Bean;

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

public class ShowCity extends LitePalSupport {
    private String city;
    private String temp;
    private String weather;
    private String location;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
