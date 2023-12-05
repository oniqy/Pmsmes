package com.example.pmsmes.Controller;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pmsmes.Adapter.AdapterMember;
import com.example.pmsmes.Adapter.AdapterStage;
import com.example.pmsmes.Adapter.ItemMoveStage;
import com.example.pmsmes.ItemAdapter.ItemStage;
import com.example.pmsmes.ItemAdapter.Project;
import com.example.pmsmes.ItemAdapter.Stage;
import com.example.pmsmes.ItemAdapter.Tag;
import com.example.pmsmes.ItemAdapter.Task;
import com.example.pmsmes.ItemAdapter.User;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectWorkspaceActivity extends AppCompatActivity {
    RecyclerView recyc_Stage;
    ArrayList<ItemStage> itemStages = new ArrayList<>();
    AdapterStage adapterStage;
    ImageButton img_buttonOption;
    String stageName;
    String getEmail;
    ArrayList<Stage> projectStageList = new ArrayList<>();
    ArrayList<Task> projectTaskList = new ArrayList<>();
    ArrayList<User> usersList = new ArrayList<>();
    TextView projectName;
    private APIInterface apiServices;
    String projectID = "";
    Project project = new Project();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_workspace2);

        Intent i = getIntent();
        projectID = i.getStringExtra("projectID");


        addControls();
        addEvents();

        //apiServices Setup
        apiServices = APIClient.getClient().create(APIInterface.class);

        loadProjectData(projectID);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        projectStageList.clear();
        projectTaskList.clear();
        usersList.clear();
        loadProjectData(projectID);
    }

    private void addControls(){
        projectName = findViewById(R.id.projectName);
        img_buttonOption = (ImageButton) findViewById(R.id.img_buttonOption);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
    }

    private void addEvents(){

        img_buttonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsMenu(v);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                projectStageList.clear();
                projectTaskList.clear();
                usersList.clear();
                loadProjectData(projectID);
            }
        });
    }

    private void loadProjectData(String projectID){
        apiServices.getProjectByID(APIClient.getToken(getApplicationContext()),projectID).enqueue(new Callback<Object>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //Xử lý dữ liệu ở đây
                if (response.isSuccessful()){
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String strObj = gson.toJson(response.body());
                    try {
                        JSONObject apiResult = new JSONObject(strObj);

                        project.setId(apiResult.getJSONObject("data").getString("_id"));
                        project.setName(apiResult.getJSONObject("data").getString("name"));
                        project.setDescription(apiResult.getJSONObject("data").getString("description"));
                        project.setId(apiResult.getJSONObject("data").getString("_id"));

                        SimpleDateFormat formatter1=new SimpleDateFormat("dd/MM/yyyy");
                        project.setStartAt(formatter1.parse(APIClient.convertMongoDate(apiResult.getJSONObject("data").getString("startAt"))));
                        project.setEndAt(formatter1.parse(APIClient.convertMongoDate(apiResult.getJSONObject("data").getString("endAt"))));

                        ArrayList<User> members = new ArrayList<User>();
                        JSONArray memberInResult = apiResult.getJSONObject("data").getJSONArray("members");

                        for (int i=0; i< memberInResult.length(); i++){
                            User member = new User();

                            member.setName(memberInResult.getJSONObject(i).getString("name"));
                            member.setEmail(memberInResult.getJSONObject(i).getString("email"));
                            member.setId(memberInResult.getJSONObject(i).getString("id"));

                            members.add(member);

                        }
                        project.setMembers(members);
                        projectName.setText(project.getName());

                        //LOAD STAGES
                        getProjectStages(project.getId());

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void getProjectStages(String projectID){
        apiServices.getProjectStages(APIClient.getToken(getApplicationContext()), projectID).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String strObj = gson.toJson(response.body());

                    try {
                        JSONObject apiResult = new JSONObject(strObj);
                        JSONArray stagesList = apiResult.getJSONArray("data");

                        for(int i=0; i < stagesList.length(); i++) {
                            Stage stage = new Stage();

                            stage.setProject(project.getId());
                            stage.setId(stagesList.getJSONObject(i).getString("_id"));
                            stage.setName(stagesList.getJSONObject(i).getString("name"));
                            stage.setIsCancel(stagesList.getJSONObject(i).getBoolean("isCancel"));
                            stage.setIsDone(stagesList.getJSONObject(i).getBoolean("isDone"));
                            stage.setSequence(stagesList.getJSONObject(i).getInt("sequence"));
                            projectStageList.add(stage);
                        }

                        //Sort theo sequence
                        projectStageList.sort(Comparator.comparingInt(o -> o.getSequence()));
                        if (projectStageList.size()>0){
                            getStageTasks();
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


    private void getStageTasks(){
        apiServices.getProjectTask(APIClient.getToken(getApplicationContext()),
                projectStageList.get(0).getProject()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String strObj = gson.toJson(response.body());
                    APIClient.logData(response.body());
                    try {
                        JSONObject apiResult = new JSONObject(strObj);
                        JSONArray data = apiResult.getJSONArray("data");
                        projectTaskList.clear();

                        for (int i=0; i< data.length(); i++){

                            Task task = new Task();
                            task.setId(data.getJSONObject(i).getString("_id"));
                            task.setName(data.getJSONObject(i).getString("name"));

                            //Xử lý creator
                            JSONObject creatorInfo = data.getJSONObject(i).getJSONObject("creator");
                            User creatorOb = new User();
                            creatorOb.setId(creatorInfo.getString("_id"));
                            creatorOb.setName(creatorInfo.getString("name"));
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




                            //Tags
                            ArrayList<Tag> tags = new ArrayList<Tag>();
                            JSONArray tagList = data.getJSONObject(i).getJSONArray("tags");

                            for (int j =0; j< tagList.length();j++) {
                                Tag tag = new Tag();
                                tag.setId(tagList.getString(j));
//                                tag.setName(tagList.getJSONObject(i).getString("name"));
//                                tag.setProject(tagList.getJSONObject(i).getString("project"));
//                                tag.setColor(tagList.getJSONObject(i).getString("color"));

                                tags.add(tag);
                            }

                            task.setTags(tags);

                            //Add to global list;
                            //Xử lý stage
                            if (data.getJSONObject(i).has("stage")){
                                task.setStage(data.getJSONObject(i).getJSONObject("stage").getString("_id"));
                                projectTaskList.add(task);
                            }

                        }

                        setupRecycleView();
                        adapterStage.notifyDataSetChanged();

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
    private boolean isSnapHelperAttached = false;
    private void setupRecycleView(){
        //control
        recyc_Stage =(RecyclerView) findViewById(R.id.recyc_Stage);
        //set adapter recyclerView
        adapterStage =new AdapterStage(projectStageList,projectTaskList,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyc_Stage.setLayoutManager(layoutManager);

        //MoveItem
        ItemTouchHelper.Callback callback =
                new ItemMoveStage(adapterStage);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyc_Stage);
        recyc_Stage.setAdapter(adapterStage);


        //Snap item recyclerView
        if (!isSnapHelperAttached) {
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyc_Stage);
            isSnapHelperAttached = true;
        }
        adapterStage.notifyDataSetChanged();


        swipeRefreshLayout.setRefreshing(false);
    }
    private void showRemoveMemberDialog(String idMember,String email,int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_removemember, null);
        builder.setView(dialogView).setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                apiServices.removeMemberFromProject(APIClient.getToken(getApplicationContext()),projectID,idMember)
                        .enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                if (response.isSuccessful()){
                                    project.getMembers().remove(position).getId();
                                    Toast.makeText(getApplicationContext(),"Đã xóa "+email+" ra khỏi dự án",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //show option Menu
    @SuppressLint("ResourceType")
    private void showOptionsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_workspace_project, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onOptionsItemSelected(item);
                return true;
            }
        });
        popupMenu.show();
    }
    @SuppressLint({"ResourceType","MissingInflatedId"})
    private void showAddStageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_addstage, null);
        EditText edt_stageName = dialogView.findViewById(R.id.edt_stageName);
        builder.setView(dialogView)
                .setPositiveButton(R.string.AddStage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        stageName = edt_stageName.getText().toString().trim();
                        apiServices.createNewStage(APIClient.getToken(getApplicationContext()),projectID,stageName)
                                .enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        if (!stageName.isEmpty()) {
                                            ItemStage itemStage = new ItemStage();
                                            itemStage.tvStageName = stageName;
                                            itemStages.add(itemStage);
                                            //adapterStage.notifyDataSetChanged();
                                            Toast.makeText(getApplicationContext(), "New stage added",
                                                    Toast.LENGTH_LONG).show();
                                        } else {
                                            // Handle empty stageName or display a message
                                            Toast.makeText(getApplicationContext(), "Stage name cannot be empty", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "Co gi do sai sai", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @SuppressLint({"ResourceType","MissingInflatedId"})
    private void showAddMenberDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_addmember,null);
        EditText edt_email = dialogView.findViewById(R.id.edt_email);
        ListView listEmail = dialogView.findViewById(R.id.list_email);
        ListView listAavatar = dialogView.findViewById(R.id.list_avatar);
        String[] AddIdMember = {null};
        AdapterMember adapterMember = new AdapterMember(getApplicationContext(),
                R.layout.item_email,
                project.getMembers());
        listAavatar.setAdapter(adapterMember);

        apiServices.getNotInProjectUserList(APIClient.getToken(getApplicationContext()),projectID)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        getEmail = edt_email.getText().toString();
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        String strObj = gson.toJson(response.body());
                        ArrayList<User> memberNotIn = new ArrayList<>();
                        try {
                            JSONObject apiResult = new JSONObject(strObj);
                            JSONArray data = apiResult.getJSONArray("data");
                            for(int i = 0; i< data.length();i++){
                                User user = new User();
                                user.setName(data.getJSONObject(i).getString("name"));
                                user.setId(data.getJSONObject(i).getString("_id"));
                                user.setEmail(data.getJSONObject(i).getString("email"));
                                memberNotIn.add(user);
                                usersList.clear();
                                usersList.addAll(memberNotIn);
                            }
                            AdapterMember adapterMember1 = new AdapterMember(getApplicationContext(),
                                    R.layout.item_email,usersList);
                            listEmail.setAdapter(adapterMember1);
                            listEmail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    edt_email.setText(usersList.get(i).getEmail());
                                    AddIdMember[0] = usersList.get(i).getId();
                                    Toast.makeText(getApplicationContext(),usersList.get(i).getEmail(),Toast.LENGTH_LONG).show();
                                }
                            });
                            listAavatar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    showRemoveMemberDialog(project.getMembers().get(i).getId(),project.getMembers().get(i).getEmail(),i);

                                }
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"Co gi do sai sai",Toast.LENGTH_LONG).show();

                    }
                });

        builder.setView(dialogView)
                .setPositiveButton(R.string.AddMember, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        apiServices.addMemberToProject(APIClient.getToken(getApplicationContext()),projectID,AddIdMember[0])
                                .enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {

                                        Toast.makeText(getApplicationContext(),"Đã gửi lời mời",Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {

                                    }
                                });
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_addMember) {
            showAddMenberDialog();
            return true;
        } else if (itemId == R.id.menu_item_addStage) {
            showAddStageDialog();
            return true;
        } else if (itemId==R.id.menu_item_dashboard) {
            Intent intent = new Intent(getApplicationContext(), Project_Report.class);
            intent.putExtra("projectID",projectID);
            startActivity(intent);
        }

        if (itemId == R.id.menu_item_createProjectTag){
            showAddNewProjectTagDialog();
        }


        return super.onOptionsItemSelected(item);
    }

    private void showAddNewProjectTagDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectWorkspaceActivity.this);
        builder.setTitle("Add new project task");

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.row_spinner,null);

        String[] colorCode = new String[]{"danger", "primary","success", "warning"};

        EditText tagName = (EditText) layout.findViewById(R.id.editText1);

        Spinner s = (Spinner) layout.findViewById(R.id.spinner1);
        ArrayAdapter adapter = new ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, colorCode);

        s.setAdapter(adapter);

        builder.setView(layout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createNewProjectTag(String.valueOf(tagName.getText()),s.getSelectedItem().toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void createNewProjectTag(String tagName, String color){
        if (TextUtils.isEmpty(tagName)) return;

        apiServices.createProjectTag(APIClient.getToken(getApplicationContext()),
                projectID, String.valueOf(tagName),color).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    projectStageList.clear();
                    projectTaskList.clear();
                    usersList.clear();
                    loadProjectData(projectID);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Tag creation failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        getMenuInflater().inflate(R.menu.menu_workspace_project, menu);
        return super.onCreateOptionsMenu(menu);
    }
}