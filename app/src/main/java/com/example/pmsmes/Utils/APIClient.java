package com.example.pmsmes.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://pmsmes-hosting.onrender.com/";

    public static final String SharedPrefs = "PMSMEs";
    public static String getToken(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SharedPrefs, MODE_PRIVATE);
        String token = prefs.getString("token", "");
        return token;
    }

    public static String getUserID(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SharedPrefs, MODE_PRIVATE);
        String token = prefs.getString("id", null);
        return token;
    }

    public static void setToken(Context context, String token){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = context.getSharedPreferences(SharedPrefs, MODE_PRIVATE).edit();
        editor.putString("token", "Bearer " + token);
        editor.apply();
    }





    public static void setLoginInfo(Context context,String token,String account, String userID, String username, String address, String email, String birthday){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = context.getSharedPreferences(SharedPrefs, MODE_PRIVATE).edit();
        editor.putString("token", "Bearer " + token);
        editor.putString("id", userID);
        editor.putString("account", account);
        editor.putString("name", username);
        editor.putString("birthday", birthday);
        editor.putString("email", email);
        editor.putString("address", address);
        editor.putLong("LAST_LOGIN",System.currentTimeMillis());
        editor.apply();
    }

    public static void logOut(Context context){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = context.getSharedPreferences(SharedPrefs, MODE_PRIVATE).edit();
        editor.putString("token", "Bearer ");
        editor.putString("id", null);
        editor.putString("account", null);
        editor.putString("name", null);
        editor.putString("birthday", null);
        editor.putString("email", null);
        editor.putString("address", null);
        editor.putLong("LAST_LOGIN",-1);
        editor.apply();
    }

    public static String getLoggedinName(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SharedPrefs, MODE_PRIVATE);
        String name = prefs.getString("name", null);

        return name;
    }

    public static String getLoggedinID(Context context){
        SharedPreferences prefs = context.getSharedPreferences(SharedPrefs, MODE_PRIVATE);
        String id = prefs.getString("id", null);

        return id;
    }

    public static void logData(Object object){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJsonString = gson.toJson(object);
        Log.d("Akkii", prettyJsonString);
    }


    public static boolean checkLastLogin(Context context){

        SharedPreferences prefs = context.getSharedPreferences(SharedPrefs, MODE_PRIVATE);
        String lastTime = prefs.getString("id", null);
        String token = prefs.getString("token", null);

        if(!TextUtils.isEmpty(lastTime) && token.length() > 7) {
           return true;
        } else {
            return false;
        }

    }

    public static String convertMongoDate(String val){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat= new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            String finalStr = outputFormat.format(inputFormat.parse(val));
            System.out.println(finalStr);
            return finalStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static Retrofit getClient(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
