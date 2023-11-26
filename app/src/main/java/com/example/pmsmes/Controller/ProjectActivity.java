package com.example.pmsmes.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.example.pmsmes.Adapter.AdapterProject;
import com.example.pmsmes.ItemAdapter.Project;
import com.example.pmsmes.R;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProjectActivity extends AppCompatActivity {
    RecyclerView projectRecycles;
    CircleImageView projectPlus;
    AdapterProject adapterProject;
    ArrayList<Project> projects = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        // Mapped widget avoid ref null.
        mapWidget();
        adapterProject = new AdapterProject(getApplicationContext(), projects);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, LinearLayoutManager.VERTICAL, false);
        projectRecycles.setLayoutManager(manager);
        projectRecycles.setAdapter(adapterProject);
    }
    private void mapWidget() {
        projectRecycles = findViewById(R.id.projectRecycles);
        projectPlus = findViewById(R.id.projectPlus);
    }
}