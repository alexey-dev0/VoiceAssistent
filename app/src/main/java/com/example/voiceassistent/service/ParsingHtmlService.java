package com.example.voiceassistent.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

    public static String getHoliday(String dateString) {
        Date date = shortStringToDate(dateString);
        try {
            Document document = Jsoup.connect(URL).get();
            Element body = document.body();
            for (Element element : body.select("div.month_row")) {
                String elementDate = element.selectFirst("span").text();
                if (longStringToDate(elementDate).after(date)) {
                    return elementDate;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "NONE";
    }

    private static String dateToString(Date date) {
        return longSdf.format(date);
    }

    private static Date shortStringToDate(String date) {
        try {
            return shortSdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private static Date longStringToDate(String date) {
        try {
            return longSdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
