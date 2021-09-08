package com.viki.toth.barbequeTime.client;

public class TestRetrofitClient extends RetrofitClient {
    private final static String BASE_URL_MOCK = "http://127.0.0.1:8080";

    @Override
    public String getBaseUrl() {
        return BASE_URL_MOCK;
    }
}
