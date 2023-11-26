package com.example.pmsmes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pmsmes.Utils.APIInterface;

public class MainActivity extends AppCompatActivity {

    private APIInterface apiServices;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}