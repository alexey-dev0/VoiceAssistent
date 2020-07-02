package com.example.voiceassistent;

import android.content.Context;

import androidx.core.util.Consumer;

import com.example.voiceassistent.handler.QuestionDate;
import com.example.voiceassistent.handler.QuestionForecast;
import com.example.voiceassistent.handler.QuestionHandler;
import com.example.voiceassistent.handler.QuestionHelp;
import com.example.voiceassistent.handler.QuestionHoliday;
import com.example.voiceassistent.handler.QuestionStandard;
import com.example.voiceassistent.handler.QuestionTranslate;

import java.util.ArrayList;
import java.util.List;

public class AI {
    private static String defaultAnswer;
    private static List<QuestionHandler> handlers;

    public static void initialize(Context context) {
        handlers = new ArrayList<>();
        handlers.add(new QuestionHoliday(context));
        handlers.add(new QuestionStandard(context));
        handlers.add(new QuestionDate(context));
        handlers.add(new QuestionHelp(context));
        handlers.add(new QuestionForecast(context));
        handlers.add(new QuestionTranslate(context));

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
