package com.example.pmsmes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.pmsmes.Models.Login;
import com.example.pmsmes.Models.Project;
import com.example.pmsmes.Models.ProjectResponse;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}