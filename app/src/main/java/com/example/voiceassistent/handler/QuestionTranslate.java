package com.example.voiceassistent.handler;

import android.content.Context;

import androidx.core.util.Consumer;

import com.example.voiceassistent.service.TranslateToString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionTranslate extends QuestionHandler {
    public QuestionTranslate(Context context) {
        super(context);
    }

    @Override
    public Boolean handle(String question, Consumer<String> callback) {
        Pattern translatePattern = Pattern.compile("переведи (.+)");
        Matcher matcher = translatePattern.matcher(question);
        if (matcher.find()) {
            final String[] text = {matcher.group(1)};
            TranslateToString.getTranslate(text[0], callback);
            return true;
        }
        return false;
    }
}
