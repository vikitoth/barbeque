package com.viki.toth.barbequeTime.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Daily {
    private final Double rain;
    private final Temp temp;

    @JsonCreator
    public Daily(@JsonProperty("rain") Double rain, @JsonProperty("temp") Temp temp) {
        this.rain = rain;
        this.temp = temp;
    }

    public Double getRain() {
        return rain;
    }

    public Temp getTemp() {
        return temp;
    }

    @Override
    public String toString() {
        return "Daily{" +
                "rain=" + rain +
                ", temp=" + temp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Daily daily = (Daily) o;
        return Objects.equals(rain, daily.rain) && temp.equals(daily.temp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rain, temp);
    }
}
