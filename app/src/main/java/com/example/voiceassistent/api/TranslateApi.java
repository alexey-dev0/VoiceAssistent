package com.example.voiceassistent.api;

import com.example.voiceassistent.model.Translate;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranslateApi {

    @POST("translate" +
            "?key=trnsl.1.1.20200423T174354Z.2ceaec527010f736.7a0cd900c0817b835878a5d69446927e37a6143d")
    Call<Translate> getTranslate(@Query("text") String text, @Query("lang") String lang);
}
