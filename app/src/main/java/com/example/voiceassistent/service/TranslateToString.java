package com.example.voiceassistent.service;

import android.util.Log;

import androidx.core.util.Consumer;

import com.example.voiceassistent.api.TranslateApi;
import com.example.voiceassistent.model.Translate;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslateToString {
    public static void getTranslate(String text, Locale locale, final Consumer<String> callback) {
        TranslateApi api = TranslateService.getApi();
        Call<Translate> call = api.getTranslate(text, locale.getLanguage());

        call.enqueue(new Callback<Translate>() {
            @Override
            public void onResponse(Call<Translate> call, Response<Translate> response) {
                Translate result = response.body();
                if (result != null) {
                    callback.accept(result.text.get(0));
                } else {
                    callback.accept("Не могу перевести");
                    Log.w("TRANSLATE", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Translate> call, Throwable t) {
                Log.w("TRANSLATE", t.getMessage());
            }
        });
    }
}
