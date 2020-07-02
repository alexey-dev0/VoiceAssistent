package com.example.voiceassistent.handler;

import android.content.Context;

import androidx.core.util.Consumer;

import com.example.voiceassistent.R;

import java.util.HashMap;
import java.util.Map;

public class QuestionStandard extends QuestionHandler {

    private Map<String, String> answersDictionary;

    public QuestionStandard(Context context) {
        super(context);

        answersDictionary = new HashMap<>();
        String[] questions = context.getResources().getStringArray(R.array.questions_standard);
        String[] answers = context.getResources().getStringArray(R.array.answers_standard);
        for (int i = 0; i < questions.length; i++) {
            answersDictionary.put(questions[i], answers[i]);
        }
    }

    @Override
    public Boolean handle(String question, Consumer<String> callback) {
        for (String key : answersDictionary.keySet()) {
            if (question.contains(key)) {
                callback.accept(answersDictionary.get(key));
                return true;
            }
        }
        return false;
    }
}
