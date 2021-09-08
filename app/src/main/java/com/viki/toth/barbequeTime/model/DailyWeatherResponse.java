package com.viki.toth.barbequeTime.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.common.api.Api;

import java.util.List;
import java.util.Objects;

public class DailyWeatherResponse implements Api.ApiOptions {

    private final List<Daily> daily;

    @JsonCreator
    public DailyWeatherResponse(@JsonProperty("daily") List<Daily> daily) {
        this.daily = daily;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    @Override
    public String toString() {
        return "DailyWeatherResponse{" +
                "daily=" + daily +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWeatherResponse that = (DailyWeatherResponse) o;
        return daily.equals(that.daily);
    }

    @Override
    public int hashCode() {
        return Objects.hash(daily);
    }
}
