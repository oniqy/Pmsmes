package com.example.pmsmes.TESTAPIACTIVITY;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pmsmes.Models.Login;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APITest extends AppCompatActivity {
    private APIInterface apiServices;
    Button loginBtn, getAllProjectBtn,getProjectByID, getProjectTaskBtn,createNewTaskBtn,updateProjectTaskBtn,
            getProjectStageBtn,createNewStageBtn;
    ListView eventsList;
    ArrayList<String> data = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String testProjectID = "6555e26489bc2f849bb81cbd";
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
        getProjectTaskBtn = findViewById(R.id.getProjectTaskBtn);
        createNewTaskBtn = findViewById(R.id.createNewTaskBtn);
        updateProjectTaskBtn = findViewById(R.id.updateProjectTaskBtn);
        getProjectStageBtn = findViewById(R.id.getProjectStageBtn);
        createNewStageBtn = findViewById(R.id.createNewStageBtn);

    }
    private void addEvents(){
        createNewStageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewStage();
            }
        });
        getProjectStageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProjectStage();
            }
        });
        updateProjectTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProjectTask();
            }
        });


        createNewTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewTask();
            }
        });

        getProjectTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProjectTask();
            }
        });

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

    private void createNewStage(){
        String stageName ="Test stage from Android";
        apiServices.createNewStage(APIClient.getToken(this),testProjectID, stageName).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    logData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("Akkii", t.getMessage());
            }
        });
    }

    private void getProjectStage(){
        apiServices.getProjectStages(APIClient.getToken(this), testProjectID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    logData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("Akkii", t.getMessage());
            }
        });
    }

    private void updateProjectTask(){
        String sampleTaskName = "Created From android and edited";
        String sampleCreatorID = "655374e35a5af92bb155bd9e";
        String sampleTaskID = "656146de556cd14dd1e6e2cd";

        JsonObject infoToUpdate = new JsonObject ();
        infoToUpdate.addProperty("name", sampleTaskName);
        infoToUpdate.addProperty("task", sampleTaskID);
        apiServices.updateProjectTask(APIClient.getToken(this),testProjectID, infoToUpdate).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    logData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("Akkii", t.getMessage());
            }
        });


    }
    private void createNewTask(){
        String sampleTaskName = "Task created from Android";
        String sampleCreatorID = "655374e35a5af92bb155bd9e";
        String sampleStageID = "65538f4bf63cf027b238c298";
        apiServices.createNewTask(APIClient.getToken(this), testProjectID,sampleTaskName,sampleCreatorID,sampleStageID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    logData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("Akkii", t.getMessage());
            }
        });
    }

    private void getProjectTask(){
        apiServices.getProjectTask(APIClient.getToken(this),testProjectID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){

                    logData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("Akkii", t.getMessage());
            }
        });
    }

    private void Login(){

        apiServices
                .login("akkii0609","daylamatkhausieubaomat")
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(APITest.this, "Đăng nhập thành công!!", Toast.LENGTH_SHORT).show();
                            logData(response.body());
                            APIClient.setToken(APITest.this, response.body().getToken());
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
        apiServices.getProjectByID(APIClient.getToken(this),id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Object res = response.body();

                if (res !=null){
                    logData(res);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void logData(Object object){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJsonString = gson.toJson(object);
        Log.d("Akkii", prettyJsonString);
    }
    private void getAllProject(){

        apiServices.getAllProjects(APIClient.getToken(this)).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                if (response.isSuccessful()){
                    logData(response.body());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("AKII_Fail", t.toString());
            }
        });
    }
}