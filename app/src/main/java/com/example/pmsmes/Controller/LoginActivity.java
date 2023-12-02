package com.example.pmsmes.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.pmsmes.Adapter.AdapterStage;
import com.example.pmsmes.Adapter.ItemMoveStage;
import com.example.pmsmes.ItemAdapter.ItemStage;
import com.example.pmsmes.Models.Login;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView forgot_passBtn;
    Button loginBtn;
    EditText username_edt,password_edt;
    ProgressDialog dialog;
    private APIInterface apiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgot_passBtn = findViewById(R.id.forgot_passBtn);
        forgot_passBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPassIntent = new Intent(view.getContext(), ForgotPasswordActivity.class);
                startActivity(forgotPassIntent);
            }
        });

        apiServices = APIClient.getClient().create(APIInterface.class);

        addControls();
        addEvents();

//        if (APIClient.checkLastLogin(getApplicationContext())){
//            finish();
//            Intent projectIntent = new Intent(LoginActivity.this, ProjectActivity.class);
//            startActivity(projectIntent);
//        }

    }


    private void addControls(){

        username_edt = findViewById(R.id.username_edt);
        password_edt = findViewById(R.id.password_edt);
        loginBtn = findViewById(R.id.loginBtn);
    }

    private void addEvents(){


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = ProgressDialog.show(LoginActivity.this, "",
                        "Loading, please wait...", true);

                String username = String.valueOf(username_edt.getText());
                String password = String.valueOf(password_edt.getText());

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)){
                    login(username,password);
                }else{
                    dialog.dismiss();
                }


            }
        });
    }

    private void forgotPassword(View v){
        Intent forgotPassIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(forgotPassIntent);
    }

    private void login(String username, String password){
        apiServices.login(username,password).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                if (response.isSuccessful()){
                    Login loginObj = response.body();
                    APIClient.setLoginInfo(LoginActivity.this,
                            loginObj.getToken(),
                            loginObj.getUser().getAccount(),
                            loginObj.getUser().getId(),
                            loginObj.getUser().getName(),
                            loginObj.getUser().getAddress(),
                            loginObj.getUser().getEmail(),
                            loginObj.getUser().getBirthday());


                    Toast.makeText(LoginActivity.this,"Xin chào, " +loginObj.getUser().getName() + "! 😎", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                    Intent projectIntent = new Intent(LoginActivity.this, ProjectActivity.class);
                    startActivity(projectIntent);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this,"Có gì đó sai sai! Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
            }

        });
    }

}