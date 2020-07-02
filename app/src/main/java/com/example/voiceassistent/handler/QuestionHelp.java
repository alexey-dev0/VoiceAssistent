package com.example.voiceassistent.handler;

import android.content.Context;

import androidx.core.util.Consumer;

import com.example.voiceassistent.R;

public class QuestionHelp extends QuestionHandler {

    public QuestionHelp(Context context) {
        super(context);
    }

    @Override
    public Boolean handle(String userQuestion, final Consumer<String> callback) {
        String question = context.getResources().getString(R.string.question_help);
        if (userQuestion.contains(question)) {
            String answer = context.getResources().getString(R.string.answer_help);
            callback.accept(answer);
            return true;
        }
        return false;
    }
}
