package com.example.pmsmes.API;


import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {


    public static final String Pref = "dummy-key-pref";
    public static final String URL = "https://pmsmes-hosting.onrender.com/";

    // Builder
    private Retrofit __builder__() {
        return new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public String getToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Pref, Context.MODE_PRIVATE);
        return pref.getString("token", "");
    }

    public void setToken(Context context, String token) {
        SharedPreferences.Editor editor = (SharedPreferences.Editor) context.getSharedPreferences(Pref, Context.MODE_PRIVATE);
        editor.putString("token", token);
        editor.apply();
    }

    public API __instance__() throws IllegalAccessException, InstantiationException {
        Retrofit retrofit = __builder__();
        return retrofit.create(API.class);
    }
}
