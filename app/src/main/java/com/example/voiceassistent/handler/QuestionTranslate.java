package com.example.voiceassistent.handler;

import android.content.Context;

import androidx.core.util.Consumer;

import com.example.voiceassistent.R;
import com.example.voiceassistent.service.TranslateToString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionTranslate extends QuestionHandler {
    public QuestionTranslate(Context context) {
        super(context);
    }

    @Override
    public Boolean handle(String question, Consumer<String> callback) {
        String msgTranslate = context.getResources().getString(R.string.question_translate);
        Pattern translatePattern = Pattern.compile(msgTranslate + " (.+)");
        Matcher matcher = translatePattern.matcher(question);
        if (matcher.find()) {
            final String[] text = {matcher.group(1)};
            TranslateToString.getTranslate(text[0], locale, callback);
            return true;
        }
        return false;
    }
}
