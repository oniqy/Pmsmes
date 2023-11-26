package com.example.pmsmes.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView forgot_passBtn;
    Button sendBtn;
    EditText edt_Email;
    private APIInterface apiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        apiServices = APIClient.getClient().create(APIInterface.class);

        addControls();
        addEvents();
    }


    private void addControls(){
        sendBtn = findViewById(R.id.sendBtn);
        edt_Email = findViewById(R.id.edt_Email);
    }

    private void addEvents(){
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBtn.setEnabled(false);
                String email = String.valueOf(edt_Email.getText());
                if (patternMatches(email)){
                    sendEmail(email);
                }

            }
        });
    }

    private void sendEmail(String email){

        apiServices.forgotPassword(email).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Đã gủi email xác nhận đến " + email,Toast.LENGTH_SHORT).show();
                    ForgotPasswordActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(ForgotPasswordActivity.this,"Có gì đó sai sai, vui lòng kiểm tra lại",Toast.LENGTH_SHORT).show();
                Log.d("akki", t.getMessage());
                sendBtn.setEnabled(true);
            }
        });

    }

    public boolean patternMatches(String emailAddress) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

}