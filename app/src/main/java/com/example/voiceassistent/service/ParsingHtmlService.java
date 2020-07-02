package com.example.voiceassistent.service;

import android.util.Log;

import org.joda.time.DateTimeComparator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParsingHtmlService {
    private static final String URL = "http://mirkosmosa.ru/holiday/2020";
    private static SimpleDateFormat longSdf = new SimpleDateFormat("dd MMMM yyyy",
            new Locale("ru"));
    private static SimpleDateFormat shortSdf = new SimpleDateFormat("dd.MM.yyyy",
            new Locale("ru"));

    public static String getHoliday(Date date) {
        Log.w("PARSE", date.toString());
        try {
            Document document = Jsoup.connect(URL).get();
            Element body = document.body();
            for (Element element : body.select("div.month_row")) {
                String elementDate = element.selectFirst("span").text();

                if (DateTimeComparator.getDateOnlyInstance()
                        .compare(longStringToDate(elementDate), date) == 0) {
                    Elements holidays = element.select("div.month_cel>ul>li");
                    StringBuilder result = new StringBuilder();
                    for (String holiday : holidays.eachText()) {
                        if (result.length() != 0) result.append(", ");
                        result.append(holiday);
                    }
                    return result.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date date) {
        return longSdf.format(date);
    }

    public static Date shortStringToDate(String date) {
        try {
            return shortSdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date longStringToDate(String date) {
        try {
            return longSdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
