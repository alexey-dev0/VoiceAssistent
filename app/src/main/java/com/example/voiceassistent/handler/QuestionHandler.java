package com.example.voiceassistent.handler;

import android.content.Context;
import android.os.Build;

import androidx.core.util.Consumer;

import java.util.Locale;

public abstract class QuestionHandler {
    protected Context context;
    protected Locale locale;

    public QuestionHandler(Context context) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            this.locale = context.getResources().getConfiguration().locale;
        }
    }

    public abstract Boolean handle(String question, final Consumer<String> callback);
}
