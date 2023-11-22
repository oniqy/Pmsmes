package com.example.pmsmes.TESTAPIACTIVITY;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pmsmes.Models.GetProjectByID;
import com.example.pmsmes.Models.GetProjectStages;
import com.example.pmsmes.Models.Login;
import com.example.pmsmes.Models.Project;
import com.example.pmsmes.Models.ProjectResponse;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APITest extends AppCompatActivity {
    private APIInterface apiServices;
    Button loginBtn, getAllProjectBtn,getProjectByID;
    ListView eventsList;
    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apitest);

        apiServices = APIClient.getClient().create(APIInterface.class);



        addControls();
        addEvents();

    }

    private void addControls(){
        loginBtn = findViewById(R.id.loginBtn);
        getAllProjectBtn = findViewById(R.id.getAllProjectBtn);
        eventsList = findViewById(R.id.eventsList);
        getProjectByID = findViewById(R.id.getProjectByID);
    }
    private void addEvents(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });

        getAllProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllProject();
            }
        });

        getProjectByID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProjectByID();
            }
        });
    }

    private void Login(){

        apiServices
                .login("akkii0609","daylamatkhausieubaomat")
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        Login res = response.body();
                        if (res != null){
                            Toast.makeText(APITest.this, "Đăng nhập thành công!!", Toast.LENGTH_SHORT).show();
                            Log.d("Akkii_res", res.getToken());
                            APIClient.setToken(APITest.this, res.getToken());
                            Toast.makeText(APITest.this, APIClient.getToken(APITest.this), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        Log.d("AKII_Fail", t.toString());
                    }
                });
    }

    private void getProjectByID(){

        String id = "6555e26489bc2f849bb81cbd";
        apiServices.getProjectByID(id).enqueue(new Callback<GetProjectByID>() {
            @Override
            public void onResponse(Call<GetProjectByID> call, Response<GetProjectByID> response) {
                GetProjectByID res = response.body();

                if (res !=null){
                    Log.d("Akkii", res.getData().getName());
                }
            }

            @Override
            public void onFailure(Call<GetProjectByID> call, Throwable t) {

            }
        });
    }
    private void getAllProject(){

        apiServices.getAllProjects(APIClient.getToken(this)).enqueue(new Callback<ProjectResponse>() {
            @Override
            public void onResponse(Call<ProjectResponse> call, Response<ProjectResponse> response) {

                ProjectResponse res = response.body();
                if (res != null){
                    for (Project e: res.getData()) {
                        Log.d("Akii_res", e.toString());
                        data.clear();
                        data.add(e.toString());
                        adapter = new ArrayAdapter<>(APITest.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,data);
                        eventsList.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProjectResponse> call, Throwable t) {
                Log.d("AKII_Fail", t.toString());
            }
        });
    }
}