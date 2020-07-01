package com.example.voiceassistent;

import android.content.Context;

import androidx.core.util.Consumer;

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
    private static Map<String, String> answersDictionary;
    private static List<String> answers = new ArrayList<>();

    public static void initialize(Context context) {
        answersDictionary = new HashMap<>();
        String[] questions = context.getResources().getStringArray(R.array.questions);
        String[] answers = context.getResources().getStringArray(R.array.answers);
        for (int i = 0; i < questions.length; i++) {
            answersDictionary.put(questions[i], answers[i]);
        }
        defaultAnswer = context.getResources().getString(R.string.default_answer);
    }

    public static void getAnswer(String question, final Consumer<String> callback) {
        question = question.toLowerCase().replace('ё', 'е');
        for (String key : answersDictionary.keySet()) {
            if (question.contains(key)) answers.add(answersDictionary.get(key));
        }

        if (question.contains("сегодня")) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            answers.add(sdf.format(new Date()));
        } else if (question.contains("час")) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            answers.add("Сейчас " + sdf.format(new Date()));
        } else if (question.contains("день недели")) {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
            answers.add("Сегодня " + sdf.format(new Date()));
        } else if (question.contains("дней до зачета")) {
            String dateStr = "2/7/2020";
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = sdf.parse(dateStr);
                long diff = date.getTime() - new Date().getTime();
                answers.add("Дней до зачёта: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (!checkWeatherRequest(question, callback)
                && !checkTranslateRequest(question, callback)) {
            answers.add(defaultAnswer);
        }


        returnAnswers(callback);
    }

    private static Boolean checkWeatherRequest(String question, final Consumer<String> callback) {
        Pattern cityPattern = Pattern.compile("погода в городе (\\p{L}+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = cityPattern.matcher(question);
        if (matcher.find()) {
            String cityName = matcher.group(1);
            ForecastToString.getForecast(cityName, s -> {
                answers.add(s);
                returnAnswers(callback);
            });
            return true;
        }
        return false;
    }

    private static Boolean checkTranslateRequest(String question, final Consumer<String> callback) {
        Pattern translatePattern = Pattern.compile("переведи (.+)");
        Matcher matcher = translatePattern.matcher(question);
        if (matcher.find()) {
            final String[] text = {matcher.group(1)};
            TranslateToString.getTranslate(text[0], s -> {
                answers.add(s);
                returnAnswers(callback);
            });
            return true;
        }
        return false;
    }

    private static void returnAnswers(final Consumer<String> callback) {
        if (!answers.isEmpty()) {
            if (answers.size() == 1) callback.accept(answers.get(0));
            else {
                StringBuilder result = new StringBuilder();
                for (String answer : answers) {
                    result.append(answer).append(", ");
                }
                callback.accept(result.toString());
            }
            answers.clear();
        }
    }
}
