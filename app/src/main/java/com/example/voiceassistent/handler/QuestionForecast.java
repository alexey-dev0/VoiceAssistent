package com.example.voiceassistent.handler;

import android.content.Context;
import android.util.Log;

import androidx.core.util.Consumer;

import com.example.voiceassistent.R;
import com.example.voiceassistent.service.ForecastToString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionForecast extends QuestionHandler {
    public QuestionForecast(Context context) {
        super(context);
    }

    @Override
    public Boolean handle(String userQuestion, Consumer<String> callback) {
        String question = context.getResources().getString(R.string.question_forecast) + " (.+)";

        Pattern cityPattern = Pattern.compile(question, Pattern.CASE_INSENSITIVE);

        Matcher matcher = cityPattern.matcher(userQuestion);
        if (matcher.find()) {
            String cityName = matcher.group(1);

            Log.w("WEATHER", cityName);
            ForecastToString.getForecast(cityName, context, locale, callback);
            return true;
        }

        return false;
    }
}
