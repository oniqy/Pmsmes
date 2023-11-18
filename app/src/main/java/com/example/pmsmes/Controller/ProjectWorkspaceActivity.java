package com.example.pmsmes.Controller;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pmsmes.Adapter.AdapterStage;
import com.example.pmsmes.Adapter.AdapterTask;
import com.example.pmsmes.ItemAdapter.ItemStage;
import com.example.pmsmes.ItemAdapter.Item_Task;
import com.example.pmsmes.R;
import android.widget.ListView;

import java.util.ArrayList;
public class ProjectWorkspaceActivity extends AppCompatActivity {
    RecyclerView recyc;
    ArrayList<ItemStage> itemStages = new ArrayList<>();
    AdapterStage adapterStage;
    String[] lstname=new String[]{"ToDo","OnGoing","Done","Cancel"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_workspace2);
        recyc =(RecyclerView) findViewById(R.id.recyc);
        itemStages = ItemStage.inititStage(lstname);
        adapterStage =new AdapterStage(itemStages,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyc.setLayoutManager(layoutManager);
        recyc.setAdapter(adapterStage);

    }
}