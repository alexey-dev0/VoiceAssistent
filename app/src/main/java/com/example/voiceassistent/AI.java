package com.example.voiceassistent;

import android.content.Context;
import android.util.Log;

import androidx.core.util.Consumer;

import com.example.voiceassistent.handler.QuestionDate;
import com.example.voiceassistent.handler.QuestionForecast;
import com.example.voiceassistent.handler.QuestionHandler;
import com.example.voiceassistent.handler.QuestionHelp;
import com.example.voiceassistent.handler.QuestionHoliday;
import com.example.voiceassistent.handler.QuestionStandard;
import com.example.voiceassistent.handler.QuestionTranslate;
import com.example.voiceassistent.service.ForecastToString;
import com.example.voiceassistent.service.TranslateToString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AI {
    private static String defaultAnswer;
    private static List<QuestionHandler> handlers;

    public static void initialize(Context context) {
        handlers = new ArrayList<>();
        handlers.add(new QuestionStandard(context));
        handlers.add(new QuestionDate(context));
        handlers.add(new QuestionHelp(context));
        handlers.add(new QuestionForecast(context));
        handlers.add(new QuestionTranslate(context));
        handlers.add(new QuestionHoliday(context));

        defaultAnswer = context.getResources().getString(R.string.default_answer);
    }

    public static void getAnswer(String question, final Consumer<String> callback) {
        question = question.toLowerCase().replace('ё', 'е');

        for (QuestionHandler handler : handlers) {
            if (handler.handle(question, callback)) return;
        }

        callback.accept(defaultAnswer);
    }
}
