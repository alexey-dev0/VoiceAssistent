package com.example.voiceassistent.handler;

import android.content.Context;

import androidx.core.util.Consumer;

public class QuestionHoliday extends QuestionHandler {
    public QuestionHoliday(Context context) {
        super(context);
    }

    @Override
    public Boolean handle(String question, Consumer<String> callback) {
        return false;
    }
}
