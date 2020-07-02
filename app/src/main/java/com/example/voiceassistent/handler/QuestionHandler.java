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
        this.locale = getLocale(context);
    }

    public static Locale getLocale(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.getResources().getConfiguration().getLocales().get(0);
        } else {
            return context.getResources().getConfiguration().locale;
        }
    }

    public abstract Boolean handle(String question, final Consumer<String> callback);
}
