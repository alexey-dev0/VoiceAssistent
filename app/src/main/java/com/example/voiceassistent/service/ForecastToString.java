package com.example.voiceassistent.service;

import android.content.Context;
import android.util.Log;

import androidx.core.util.Consumer;

import com.example.voiceassistent.R;
import com.example.voiceassistent.api.ForecastApi;
import com.example.voiceassistent.model.Forecast;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastToString {
    public static void getForecast(String city, Context context, Locale locale, final Consumer<String> callback) {
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
                    weatherDescription.append(" outside");
                    Log.w("WEATHER", weatherDescription.toString());
                    TranslateToString.getTranslate(weatherDescription.toString(), locale, s -> {
                        String msgForecastNow = context.getResources().getString(R.string.answer_forecast_now);
                        String msgDelimiter = locale.getCountry().toLowerCase().equals("ru") ? " и " : " and ";
                        String msgDegrees = locale.getCountry().toLowerCase().equals("ru") ? degreeString(result.current.temperature) : " degrees";
                        String answer = msgForecastNow + " " + result.current.temperature
                                + msgDegrees + msgDelimiter + s.replace(" снаружи", "");
                        callback.accept(answer);
                    });
                } else {
                    String msgForecastNone = context.getResources().getString(R.string.answer_forecast_none);
                    callback.accept(msgForecastNone);
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
        } else return " градусов";
    }
}
