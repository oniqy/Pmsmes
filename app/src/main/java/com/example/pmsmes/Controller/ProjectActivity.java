package com.example.pmsmes.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pmsmes.API.API;
import com.example.pmsmes.API.RetrofitHelper;
import com.example.pmsmes.Adapter.AdapterProject;
import com.example.pmsmes.ItemAdapter.Project;
import com.example.pmsmes.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectActivity extends AppCompatActivity {
    RecyclerView projectRecycles;
    CircleImageView projectPlus;
    AdapterProject adapterProject;
    ArrayList<Project> projects = new ArrayList<>();
    RetrofitHelper retrofitHelper = new RetrofitHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        // Mapped widget avoid ref null.
        mapWidget();
        dummy();
        adapterProject = new AdapterProject(getApplicationContext(), projects);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
        projectRecycles.setLayoutManager(manager);
        projectRecycles.setAdapter(adapterProject);
        projectPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    getProjects();
                } catch (IllegalAccessException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    private void mapWidget() {
        projectRecycles = findViewById(R.id.projectRecycles);
        projectPlus = findViewById(R.id.projectPlus);
    }


    // Get data from database.
    private void getProjects() throws IllegalAccessException, InstantiationException {
        API api = retrofitHelper.__instance__();
        String token = "";
        Call<Project> call = api.PullAllProject(token);
        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                if (response.code() != 200){
                    Toast.makeText(ProjectActivity.this, "Wrong!", Toast.LENGTH_SHORT).show();
                }else {
                    String data = response.body().toString();
                    System.out.println("Samiweo");
                }
            }
            @Override
            public void onFailure(Call<Project> call, Throwable t) {
            }
        });
    }




    private void dummy() {
        for (int i = 1; i <= 10; i++) {
            Project project = new Project();
            project.setName(String.format("Oryza System %s", i));
            project.setBackground("plus");
            projects.add(project);
        }

    }


}