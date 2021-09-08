package com.viki.toth.barbequeTime.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.viki.toth.barbequeTime.client.WeatherApi.BASE_URL;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private final WeatherApi weatherApi;

    public RetrofitClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        weatherApi = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(WeatherApi.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public WeatherApi getWeatherApi() {
        return weatherApi;
    }
}
