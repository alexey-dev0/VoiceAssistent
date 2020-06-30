package com.example.voiceassistent;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

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
        question = question.toLowerCase();
        for (String key : answersDictionary.keySet())
        {
            if (question.contains(key)) return answersDictionary.get(key);
        }
        return defaultAnswer;
    }
}
