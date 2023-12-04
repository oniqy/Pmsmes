package com.example.pmsmes.Controller;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;

import com.example.pmsmes.Adapter.AdapterMember;
import com.example.pmsmes.ItemAdapter.Tag;
import com.example.pmsmes.ItemAdapter.Task;
import com.example.pmsmes.ItemAdapter.User;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class EditTask_Activity extends AppCompatActivity {
    private Spinner spinner;
    APIInterface apiServices;
    String projectID = "";
    String taskId,stageID,projectName;
    private List<String> list;
    TextView taskDirTextView,createdTextView;
    EditText taskNameEdt, taskDescriptionEdt;
    ImageButton img_buttonOption;

    public static Button pickDate;
    ListView list_assignee;
    ChipGroup tagChipGroup;
    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task_2);
        addControl();
        addEvent();
        Intent i = getIntent();

        projectID = i.getStringExtra("projectID");
        taskId = i.getStringExtra("taskID");
        stageID = i.getStringExtra("stageID");
        apiServices = APIClient.getClient().create(APIInterface.class);
        loadDetailtask();
        loadSpinnerStage();
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker.
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it.
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        @SuppressLint("SetTextI18n")
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date the user picks.
            pickDate.setText(day +"/"+month+"/"+ year);
        }

        public static String TAG = "datePicker";
    }



    private void loadSpinnerStage(){
        apiServices.getProjectStages(APIClient.getToken(getApplicationContext()),projectID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()){
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            String strObj = gson.toJson(response.body());
                            list = new ArrayList<>();
                            try {
                                JSONObject apiResult = new JSONObject(strObj);
                                JSONArray stagesList = apiResult.getJSONArray("data");
                                for(int i=0; i < stagesList.length(); i++) {

                                    if(stagesList.getJSONObject(i).getString("id").equals(stageID)){
                                        String stage =stagesList.getJSONObject(i).getString("name");
                                        list.add(stage);
                                        list.set(0,stage);
                                    }else {
                                        list.add(stagesList.getJSONObject(i).getString("name"));
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item, list);
//                        spinner.setAdapter(spinnerAdapter);

                    }
                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });


    }
    private void loadDetailtask(){

        apiServices.getTaskByID(APIClient.getToken(getApplicationContext()),
                projectID,taskId).enqueue(new Callback<Object>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String strObj = gson.toJson(response.body());
                    APIClient.logData(response.body());
                    try {
                        JSONObject apiResult = new JSONObject(strObj);
                        JSONObject data = apiResult.getJSONObject("data");

                        if (data.getString("name") !=null)
                            taskNameEdt.setText(data.getString("name"));

                        //Xu ly creator
                        String name = data.getJSONObject("creator").getString("name");
                        String email = data.getJSONObject("creator").getString("email");
                        createdTextView.setText("✒️ Created by " + name + " - " + email);

                        //Description
                        if (data.has("description"))
                            taskDescriptionEdt.setText(data.getString("description"));

                        //Dir
                        taskDirTextView.setText("\uD83C\uDFE0 in " + data.getJSONObject("stage").getString("name"));

                        //Tags
                        for (int i=0; i< data.getJSONArray("tags").length(); i++){
                            //NOT IMPLEMENTED
                            addTagsChip(data.getJSONArray("tags").getJSONObject(i).getString("name"),
                                    data.getJSONArray("tags").getJSONObject(i).getString("color"));
                        }

                        //End date

                        if (data.has("dateDeadline")){
                            pickDate.setText(data.getString("dateDeadline"));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                finish();
                Toast.makeText(getApplicationContext(),"Có gì đó sai sai!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @SuppressLint("ResourceType")
    private void addTagsChip(String s, String color){
        Chip chip = new Chip(EditTask_Activity.this);
        chip.setClickable(true);
        chip.setCheckable(false);
        chip.setText(s);
        int colorHex;

        if (!TextUtils.isEmpty(color)){
            chip.setChipBackgroundColor(AppCompatResources.getColorStateList(getApplicationContext(), R.color.warning));
        }

        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Delete Tag not implented!", Toast.LENGTH_SHORT).show();
            }
        });
        tagChipGroup.addView(chip);
    }

    private void addControl(){
        tagChipGroup = findViewById(R.id.tagChipGroup);
        taskDirTextView = findViewById(R.id.taskDirTextView);
        createdTextView = findViewById(R.id.createdTextView);
        taskNameEdt = findViewById(R.id.taskNameEdt);
        taskDescriptionEdt = findViewById(R.id.taskDescriptionEdt);
        img_buttonOption = findViewById(R.id.img_buttonOption);

        pickDate = (Button) findViewById(R.id.pickDate);

    }
    private void addEvent(){

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerFragment().show(getSupportFragmentManager(), DatePickerFragment.TAG);
            }
        });

        img_buttonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionsMenu(view);
            }
        });

    }


    private void removeTask(){

        JsonObject taskToDelete = new JsonObject();
        taskToDelete.addProperty("task", taskId);

        apiServices.removeTask(APIClient.getToken(getApplicationContext()),projectID, taskToDelete)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if(response.isSuccessful()){
                            finish();
                            Toast.makeText(getApplicationContext(),"Task removed!",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Có gì đó sai sai!",Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void AssignTask(){
        String userID = APIClient.getUserID(getApplicationContext());
        String[] userList = {userID};
        apiServices.assignTask(APIClient.getToken(getApplicationContext()),projectID,userList)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Assign success",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_saveTask) {
            saveTask();
            return true;
        }
        else if (itemId == R.id.menu_item_removeTask){
            removeTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTask(){

        JsonObject taskToSave = new JsonObject();
        taskToSave.addProperty("task", taskId);
        taskToSave.addProperty("name", String.valueOf(taskNameEdt.getText()));
        taskToSave.addProperty("description", String.valueOf(taskDescriptionEdt.getText()));
        taskToSave.addProperty("dateDeadline", String.valueOf(pickDate.getText()));
        apiServices.updateProjectTask(APIClient.getToken(getApplicationContext()),
                projectID, taskToSave).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    finish();
                    Toast.makeText(getApplicationContext(),"Task saved!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Có gì đó sai sai!",Toast.LENGTH_SHORT).show();
                Log.d("AKKi", t.getMessage());
            }
        });
    }

    private void showOptionsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.option_task, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onOptionsItemSelected(item);
                return true;
            }
        });
        popupMenu.show();
    }
}