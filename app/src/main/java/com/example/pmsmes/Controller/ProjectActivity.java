package com.example.pmsmes.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pmsmes.Adapter.AdapterProject;
import com.example.pmsmes.ItemAdapter.Project;
import com.example.pmsmes.ItemAdapter.User;
import com.example.pmsmes.Models.Login;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectActivity extends AppCompatActivity {
    RecyclerView projectRecycles;
    CircleImageView projectPlus;
    AdapterProject adapterProject;
    ArrayList<Project> projects = new ArrayList<>();
    Dialog dialog;
    APIInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        mapWidget();
        showDialog();
        getAllProject();
        //dummy();
    }

    private void mapWidget() {
        projectRecycles = findViewById(R.id.projectRecycles);
        projectPlus = findViewById(R.id.projectPlus);
        adapterProject = new AdapterProject(getApplicationContext(), projects);
        api = APIClient.getClient().create(APIInterface.class);
    }

    private void showDialog() {
        projectPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(view.getContext());
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.Base_Theme_Pmsmes;
                EditText dialogProjectName = dialog.findViewById(R.id.dialogProjectName);
                Button dialogCreate = dialog.findViewById(R.id.dialogCreate);
                Button dialogCancel = dialog.findViewById(R.id.dialogCancel);
                disableButton(dialogProjectName, dialogCreate);
                dialogCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Create project
                        createProject(dialogProjectName.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void disableButton(EditText edit, Button button) {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("")) {
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                button.setEnabled(!TextUtils.isEmpty(charSequence.toString().trim()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getAllProject() {
        String userID = APIClient.getUserID(getApplicationContext());
        String token = APIClient.getToken(getApplicationContext());
        if (userID == null || token == "Bearer ") return;
        api.getMyProject(token, userID).enqueue(new Callback<Object>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String strObj = gson.toJson(response.body());
                ArrayList<Project> projects1 = new ArrayList<>();
                try {
                    JSONObject arrObjects = new JSONObject(strObj);
                    for (int i = 0; i < arrObjects.getJSONArray("data").length(); i++) {
                        Project project = new Project();
                        JSONObject data = arrObjects.getJSONArray("data").getJSONObject(i);
                        project.setName(data.getString("name"));
                        project.setDescription(data.getString("description"));
                        project.setId(data.getString("id"));
                        projects1.add(project);
                        projects.clear();
                        projects.addAll(projects1);
                        adapterProject = new AdapterProject(getApplicationContext(),projects);
                        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
                        projectRecycles.setLayoutManager(manager);
                        projectRecycles.setAdapter(adapterProject);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });

    }

    private void dummy() {
        for (int i = 1; i <= 10; i++) {
            Project project = new Project();
            project.setName(String.format("Dự án %s", i));
            projects.add(project);
        }
        adapterProject = new AdapterProject(getApplicationContext(),projects);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
        projectRecycles.setLayoutManager(manager);
        projectRecycles.setAdapter(adapterProject);
    }

    private void createProject(String name) {

        api.createNewProject(APIClient.getToken(getApplicationContext()), name, "", APIClient.getUserID(getApplicationContext())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    getAllProject();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Có gì đó sai sai! Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
            }
        });

    }


}