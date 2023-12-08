package com.example.pmsmes.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pmsmes.Adapter.AdapterReport;
import com.example.pmsmes.ItemAdapter.Item_Report;
import com.example.pmsmes.ItemAdapter.Task;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Project_Report extends AppCompatActivity {
    GridView gridViewInfo;
    ArrayList<Item_Report> item_reports = new ArrayList<>();
    AdapterReport adapterReport;
    APIInterface apiServices;
    String projectID = "";
    ProgressBar progressBar_Task,progressBar_TaskCancel;
    TextView tvProgress,tvProgressCancel;
    ArrayList<Task> taskDone = new ArrayList<>();
    ArrayList<Task> taskExpired = new ArrayList<>();

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
        tvProgress = findViewById(R.id.tvProgress);
        tvProgressCancel = findViewById(R.id.tvProgressCancel);
        gridViewInfo = findViewById(R.id.gridViewInfo);
        progressBar_Task = findViewById(R.id.progressBar_Task);
        progressBar_TaskCancel = findViewById(R.id.progressBar_TaskCancel);
    }
    @SuppressLint("ObjectAnimatorBinding")
    private void setProgressBar_Task(){
        progressBar_Task.setMax((int)tasks.size());
        progressBar_Task.setProgress((int) taskDone.size());
        ValueAnimator animator = ValueAnimator.ofInt(progressBar_Task.getProgress(), taskDone.size());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                progressBar_Task.setProgress(animatedValue);
                tvProgress.setText(animatedValue + "/" + tasks.size()+"\nDone");
            }
        });
        animator.setDuration(5000);
        animator.start();
        progressBar_TaskCancel.setMax((int)tasks.size());
        progressBar_TaskCancel.setProgress((int) taskCancel.size());
        ValueAnimator animator2 = ValueAnimator.ofInt(progressBar_TaskCancel.getProgress(), taskCancel.size());
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator2) {
                int animatedValue = (int) animator2.getAnimatedValue();
                progressBar_TaskCancel.setProgress(animatedValue);
                tvProgressCancel.setText(animatedValue + "/" + tasks.size()+"\nCancel");
            }
        });
        animator2.setDuration(5000);
        animator2.start();
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
                                tasks.clear();
                                for(int i = 0;i<array.length();i++){
                                    Task task = new Task();
                                    task.setId(array.getJSONObject(i).getString("_id"));
                                    task.setName(array.getJSONObject(i).getString("name"));
                                    if (array.getJSONObject(i).has("stage")){
                                        task.setStage(array.getJSONObject(i).getJSONObject("stage").getString("_id"));
                                        tasks.add(task);
                                    }

                                    if (array.getJSONObject(i).has("stage")){
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


                                countTaskList.add(tasks.size());
                                countTaskList.add(taskDone.size());
                                countTaskList.add(taskCancel.size());
                                setProgressBar_Task();

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