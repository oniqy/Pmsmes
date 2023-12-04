package com.example.pmsmes.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.example.pmsmes.Adapter.AdapterReport;
import com.example.pmsmes.ItemAdapter.Item_Report;
import com.example.pmsmes.ItemAdapter.Stage;
import com.example.pmsmes.ItemAdapter.Task;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Project_Report extends AppCompatActivity {
    GridView gridViewInfo;
    ArrayList<Item_Report> item_reports = new ArrayList<>();
    AdapterReport adapterReport;
    APIInterface apiServices;
    String projectID = "";
    ArrayList<Task> taskDone = new ArrayList<>();
    ArrayList<Task> taskCancel = new ArrayList<>();

    ArrayList<Task> tasks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_report);

        Intent intent = getIntent();
        projectID = intent.getStringExtra("projectID");
        apiServices = APIClient.getClient().create(APIInterface.class);
        addControl();
        getStatusTask();
    }

    private void addControl(){
        gridViewInfo = findViewById(R.id.gridViewInfo);
    }
    private void getStatusTask(){
        apiServices.getProjectTask(APIClient.getToken(getApplicationContext()),projectID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if(response.isSuccessful()){
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            String strObj = gson.toJson(response.body());
                            try {
                                JSONObject apiResult = new JSONObject(strObj);
                                JSONArray array = apiResult.getJSONArray("data");
                                for(int i = 0;i<array.length();i++){
                                    Task task = new Task();
                                    task.setId(array.getJSONObject(i).getString("_id"));
                                    task.setName(array.getJSONObject(i).getString("name"));
                                    tasks.add(task);
                                    if (array.getJSONObject(i).has("stage")){
                                        task.setStage(array.getJSONObject(i).getJSONObject("stage").getString("isDone"));
                                        Boolean statusDone = Boolean.valueOf(array.getJSONObject(i).getJSONObject("stage").getString("isDone"));
                                        Boolean statusCancel = Boolean.valueOf(array.getJSONObject(i).getJSONObject("stage").getString("isCancel"));
                                        if(statusDone == true){
                                            taskDone.add(task);
                                        }
                                        if(statusCancel == true){
                                            taskCancel.add(task);
                                        }
                                    }

                                }
                                List<String> infoList = new ArrayList<>();
                                List<Integer> countTaskList = new ArrayList<>();


                                infoList.add("All Task");
                                infoList.add("Task Done");
                                infoList.add("Task cancel");
                                System.out.println("Tasks: " + tasks.size());
                                System.out.println("Task Done: " + taskDone.size());
                                System.out.println("Task Cancel: " + taskCancel.size());
                                countTaskList.add(tasks.size());
                                countTaskList.add(taskDone.size());
                                countTaskList.add(taskCancel.size());


                                GridViewAdapter(infoList, countTaskList);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

    }
    private void GridViewAdapter(List<String> infoList, List<Integer> countTaskList) {
        item_reports.clear(); // Clear the existing data
        int size = Math.min(infoList.size(), countTaskList.size());

        for (int i = 0; i < size; i++) {
            Item_Report item_report = new Item_Report();
            item_report.setInfo(infoList.get(i));
            item_report.setCount_rs(countTaskList.get(i));
            item_reports.add(item_report);
        }

        // Set up the adapter and notify the changes
        if (adapterReport == null) {
            adapterReport = new AdapterReport(Project_Report.this, R.layout.item_gridview_report, item_reports);
            gridViewInfo.setAdapter(adapterReport);
        } else {
            adapterReport.notifyDataSetChanged();
        }
    }
}