package com.example.voiceassistent.handler;

import android.content.Context;
import android.content.res.Resources;

import androidx.core.util.Consumer;

import com.example.voiceassistent.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class QuestionDate extends QuestionHandler {

    private static final String dateX = "2/7/2020";

    public QuestionDate(Context context) {
        super(context);
    }

    @Override
    public Boolean handle(String userQuestion, Consumer<String> callback) {
        String pattern = "";
        String prefix = "";

        Resources resources = context.getResources();

        String question_today = context.getResources().getString(R.string.question_today);
        String question_dayOfWeek = resources.getString(R.string.question_dayOfWeek);
        String question_hour = resources.getString(R.string.question_hour);
        String question_by_x_day = resources.getString(R.string.question_by_x_day);

        boolean quest_by_dayX = false;

        if (userQuestion.contains(question_today)) {
            pattern = "dd.MM.yyyy";
        } else if (userQuestion.contains(question_dayOfWeek)) {
            pattern = "HH:mm";
            prefix = resources.getString(R.string.answer_dayOfWeek);
        } else if (userQuestion.contains(question_hour)) {
            pattern = "EEEE";
            prefix = resources.getString(R.string.answer_hour);
        } else if (userQuestion.contains(question_by_x_day)) {
            quest_by_dayX = true;
            pattern = "dd/MM/yyyy";
            prefix = resources.getString(R.string.answer_by_x_day);
        }

        if (!pattern.equals("")) {
            String date = getDate(new SimpleDateFormat(pattern, locale), quest_by_dayX);
            String answer = prefix + " " + date;
            callback.accept(answer);
            return true;
        }

        return false;
    }

    private String getDate(SimpleDateFormat sdf, boolean quest_by_dayX) {
        if (quest_by_dayX) {
            try {
                Date date_x = sdf.parse(dateX);
                long diff = date_x.getTime() - new Date().getTime();
                return Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return sdf.format(new Date());
    }
}
