package com.viki.toth.barbequeTime.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Temp {
    private final double day;

    @JsonCreator
    public Temp(@JsonProperty("day") double day) {
        this.day = day;
    }

    public double getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "Temp{" +
                "day=" + day +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Temp temp = (Temp) o;
        return Double.compare(temp.day, day) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day);
    }
}
