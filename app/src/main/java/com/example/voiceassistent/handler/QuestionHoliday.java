package com.example.voiceassistent.handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.util.Consumer;

import com.example.voiceassistent.R;
import com.example.voiceassistent.service.ParsingHtmlService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionHoliday extends QuestionHandler {
    public QuestionHoliday(Context context) {
        super(context);
    }

    @Override
    public Boolean handle(String question, Consumer<String> callback) {
        String msgHoliday = context.getString(R.string.question_holiday);

        if (question.contains(msgHoliday)) {
            String msgToday = context.getString(R.string.question_holiday_today);
            String msgTomorrow = context.getString(R.string.question_holiday_tomorrow);
            String msgYesterday = context.getString(R.string.question_holiday_yesterday);

            Matcher matcher = Pattern.compile(msgHoliday + " ([^,]+)").matcher(question);

            List<String> requests = new ArrayList<>();
            while (matcher.find()) requests.add(matcher.group());
            if (requests.isEmpty()) return false;

            List<Date> dates = new ArrayList<>();
            for (String request : requests) {
                if (request.contains(msgToday)) dates.add(new Date());
                else if (request.contains(msgTomorrow)) dates.add(getTomorrow());
                else if (request.contains(msgYesterday)) dates.add(getYesterday());
                else {
                    Date date = ParsingHtmlService.shortStringToDate(request.trim());
                    if (date != null) dates.add(date);
                }
            }

            @SuppressLint("StaticFieldLeak") AsyncTask<List<Date>, Void, StringBuilder> task = new AsyncTask<List<Date>, Void, StringBuilder>() {
                @Override
                protected StringBuilder doInBackground(List<Date>... holiday_dates) {
                    StringBuilder result = new StringBuilder();
                    boolean first = true;
                    for (Date date : holiday_dates[0]) {
                        String name = ParsingHtmlService.getHoliday(date);
                        if (first) first = false;
                        else result.append(",\n");
                        result.append(ParsingHtmlService.dateToString(date));
                        result.append(" - ");
                        if (name == null) result.append(context.getString(R.string.answer_holiday_not_found));
                        else result.append(name);
                    }
                    return result;
                }

                @Override
                protected void onPostExecute(StringBuilder result) {
                    super.onPostExecute(result);
                    if (dates.size() != requests.size()) {
                        result.append("\n").append(context.getString(R.string.answer_holiday_not_all));
                    }
                    callback.accept(result.toString());
                }
            };
            task.execute(dates);
            Log.w("PARSE", "task execute");
            return true;
        }

        return false;
    }

    private Date getTomorrow() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    private Date getYesterday() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        return c.getTime();
    }
}
