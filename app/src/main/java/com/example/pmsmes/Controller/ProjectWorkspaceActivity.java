package com.example.pmsmes.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pmsmes.Adapter.AdapterTask;
import com.example.pmsmes.ItemAdapter.Item_Task;
import com.example.pmsmes.R;
import android.widget.ListView;

import java.util.ArrayList;
public class ProjectWorkspaceActivity extends AppCompatActivity {
    ListView lsv_Task;
    ArrayList<Item_Task> itemTask = new ArrayList<>();
    AdapterTask adapterTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_workspace2);
        lsv_Task = findViewById(R.id.lsv_Task);
        Item_Task task = new Item_Task();

        ArrayList<String> list = new ArrayList<String>();
        // thêm các phần tử vào list
        list.add("Java");
        list.add("C++");
        for (int i = 0;i<list.size();i++){

            task.name = list.get(i);
            itemTask.add(task);
        }

        adapterTask = new AdapterTask(getApplicationContext(), R.layout.task_item, itemTask);
        lsv_Task.setAdapter(adapterTask);
    }
}