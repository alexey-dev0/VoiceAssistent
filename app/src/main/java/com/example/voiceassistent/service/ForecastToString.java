package com.example.voiceassistent.service;

import android.util.Log;

import androidx.core.util.Consumer;

import com.example.voiceassistent.api.ForecastApi;
import com.example.voiceassistent.model.Forecast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastToString {
    public static void getForecast(String city, final Consumer<String> callback) {
        ForecastApi api = ForecastService.getApi();
        Call<Forecast> call = api.getCurrentWeather(city);

        call.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                Forecast result = response.body();
                if (result != null && result.current != null) {
                        StringBuilder weatherDescription = new StringBuilder();
                        for (String desc : result.current.weather_descriptions) {
                            if (weatherDescription.length() > 0) weatherDescription.append(", ");
                            weatherDescription.append(desc.toLowerCase());
                        }
                        TranslateToString.getTranslate(weatherDescription.toString(), s -> {
                            String answer = "Сейчас где-то " + result.current.temperature
                                    + degreeString(result.current.temperature) + " и " + s;
                            callback.accept(answer);
                        });
                } else {
                    callback.accept("Не могу узнать погоду");
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Log.w("WEATHER", t.getMessage());
            }
        });
    }

    private static String degreeString(Integer degree) {
        degree = Math.abs(degree);
        if (degree < 10 || degree > 20) {
            if (degree % 10 == 1) return " градус";
            else if (degree % 10 == 2 || degree % 10 == 3 || degree % 10 == 4)
                return " градуса";
            else return " градусов";
        }
        else return " градусов";
    }
}
