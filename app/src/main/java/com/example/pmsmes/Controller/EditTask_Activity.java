package com.example.pmsmes.Controller;
import com.example.pmsmes.Adapter.AdapterMember;
import com.example.pmsmes.ItemAdapter.Stage;
import com.example.pmsmes.ItemAdapter.Tag;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.pmsmes.ItemAdapter.User;
public class EditTask_Activity extends AppCompatActivity {
    private Spinner spinner;
    APIInterface apiServices;
    String projectID = "";
    String taskId,stageID;
    private List<String> list;
    TextView ET_taskName,ET_createdName;
    EditText ET_taskName_1;
    ImageButton btnoption,btn_back,imgBtn_addPeople;
    Button btn_save;
    ListView list_assignee;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        addControl();
        addEvent();
        Intent i = getIntent();
        projectID = i.getStringExtra("projectID");
        taskId = i.getStringExtra("taskID");
        ET_taskName.setText(i.getStringExtra("taskName"));
        stageID = i.getStringExtra("stageID");
        apiServices = APIClient.getClient().create(APIInterface.class);
        loadDetailtask();
        loadSpinnerStage();
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
                        spinner.setAdapter(spinnerAdapter);

                    }
                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });


    }
    private void loadDetailtask(){
        apiServices.getProjectTask(APIClient.getToken(getApplicationContext()),projectID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            String strObj = gson.toJson(response.body());
                            try {
                                JSONObject apiResult = new JSONObject(strObj);
                                JSONArray data = apiResult.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject taskObject = data.getJSONObject(i);
                                    String idtask = taskObject.getString("_id");
                                    if (idtask.equals(taskId)) {
                                        Task task = new Task();
                                        task.setName(data.getJSONObject(i).getString("name"));
                                        ET_taskName_1.setText(task.getName());
                                        //Xử lý creator
                                        JSONObject creatorInfo = data.getJSONObject(i).getJSONObject("creator");
                                        User creatorOb = new User();
                                        creatorOb.setId(creatorInfo.getString("_id"));
                                        creatorOb.setName(creatorInfo.getString("name"));
                                        ET_createdName.setText(creatorOb.getName());
                                        creatorOb.setEmail(creatorInfo.getString("email"));
                                        task.setCreator(creatorOb);
                                        //Xử lý assignee
                                        ArrayList<User> assignee = new ArrayList<User>();
                                        JSONArray assigneeList = data.getJSONObject(i).getJSONArray("assignee");

                                        for (int j =0; j< assigneeList.length();j++) {
                                            User member = new User();
                                            member.setId(assigneeList.getJSONObject(i).getString("_id"));
                                            member.setName(assigneeList.getJSONObject(i).getString("name"));
                                            member.setEmail(assigneeList.getJSONObject(i).getString("email"));
                                            assignee.add(member);
                                        }
                                        task.setAssignee(assignee);
                                        AdapterMember adapterMember = new AdapterMember(getApplicationContext(),
                                                R.layout.item_email,
                                                task.getAssignee());
                                        list_assignee.setAdapter(adapterMember);
                                        //Tags
                                        ArrayList<Tag> tags = new ArrayList<Tag>();
                                        JSONArray tagList = data.getJSONObject(i).getJSONArray("tags");

                                        for (int j =0; j< tagList.length();j++) {
                                            Tag tag = new Tag();
                                            tag.setId(tagList.getJSONObject(i).getString("_id"));
                                            tag.setName(tagList.getJSONObject(i).getString("name"));
                                            tag.setProject(tagList.getJSONObject(i).getString("project"));
                                            tag.setColor(tagList.getJSONObject(i).getString("color"));
                                            tags.add(tag);
                                        }
                                        task.setTags(tags);
                                        if (data.getJSONObject(i).has("stage")){
                                            task.setStage(data.getJSONObject(i).getJSONObject("stage").getString("_id"));

                                        }
                                    }
                                }
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
    private void addControl(){
         ET_taskName = (TextView) findViewById(R.id.ET_taskName);
         ET_taskName_1 = (EditText) findViewById(R.id.ET_taskName_1);
        imgBtn_addPeople = (ImageButton) findViewById(R.id.imgBtn_addPeople);
         ET_createdName = (TextView) findViewById(R.id.ET_createdName);
         btnoption = (ImageButton)findViewById(R.id.btnoption);
        list_assignee = (ListView)findViewById(R.id.list_assignee);
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        spinner = (Spinner) findViewById(R.id.ET_stageName);
    }
    private void addEvent(){
        btnoption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsMenu(v);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProjectWorkspaceActivity.class);
                startActivity(intent);
            }
        });
        imgBtn_addPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void removeTask(){
        apiServices.removeTask(APIClient.getToken(getApplicationContext()),projectID,taskId)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Remove success",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

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
        if (itemId == R.id.menu_item_removeTask) {
            removeTask();
            return true;
        }
        else if (itemId == R.id.menu_item_assignTask){
            AssignTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
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