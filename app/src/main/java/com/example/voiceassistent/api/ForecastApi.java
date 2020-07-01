package com.example.voiceassistent.api;

import com.example.voiceassistent.model.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForecastApi {
    @GET("/current?access_key=94c02871732b6f882007e0711eab9a79")
    Call<Forecast> getCurrentWeather(@Query("query") String city);
}
