package com.viki.toth.barbequeTime.client;

import com.viki.toth.barbequeTime.model.DailyWeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    String API_KEY = "6939e4e8f6db42a2428b700b72bcefb4";
    String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    @GET("onecall?mode=json&units=metric&exclude=hourly,current,minutely&appid=" + API_KEY)
    Call<DailyWeatherResponse> getDailyWeather(@Query("lat") double lat, @Query("lon") double lon);
}
