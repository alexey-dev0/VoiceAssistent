package com.example.voiceassistent;

import android.content.Context;
import android.os.Build;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AI {
    private static String defaultAnswer;
    private static Map<String, String> answersDictionary;

    public static void initialize(Context context) {
        answersDictionary = new HashMap<>();
        String[] questions = context.getResources().getStringArray(R.array.questions);
        String[] answers = context.getResources().getStringArray(R.array.answers);
        for (int i = 0; i < questions.length; i++) {
            answersDictionary.put(questions[i], answers[i]);
        }
        defaultAnswer = context.getResources().getString(R.string.default_answer);
    }

    public static String getAnswer(String question) {
        question = question.toLowerCase().replace('ё', 'е');
        for (String key : answersDictionary.keySet())
        {
            if (question.contains(key)) return answersDictionary.get(key);
        }

        if (question.contains("сегодня")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            return "Сегодня " + sdf.format(new Date());
        } else if (question.contains("час")) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return "Сейчас " + sdf.format(new Date());
        } else if (question.contains("день недели")) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
            return "Сегодня " + sdf.format(new Date());
        } else if (question.contains("дней до зачета")) {
            String dateStr = "2/7/2020";
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = sdf.parse(dateStr);
                long diff = date.getTime() - new Date().getTime();
                return "Дней до зачёта: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return defaultAnswer;
    }
}
