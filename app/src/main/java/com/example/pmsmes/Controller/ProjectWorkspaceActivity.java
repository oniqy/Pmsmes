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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.PopupMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import androidx.recyclerview.widget.SnapHelper;

import com.example.pmsmes.Adapter.AdapterMember;
import com.example.pmsmes.Adapter.AdapterStage;
import com.example.pmsmes.Adapter.ItemMoveStage;
import com.example.pmsmes.ItemAdapter.ItemStage;
import com.example.pmsmes.ItemAdapter.Project;
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
    ArrayList<String> lstStage = new ArrayList<>();
    ArrayList<String> lstname= new ArrayList<>();
    TextView projectName;
    private APIInterface apiServices;
    String projectID = "";
    Project project = new Project();

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


    private void addControls(){
        projectName = findViewById(R.id.projectName);
        img_buttonOption = (ImageButton) findViewById(R.id.img_buttonOption);
    }

    private void addEvents(){

        img_buttonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsMenu(v);
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

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void setupRecycleView(){
        //control
        recyc_Stage =(RecyclerView) findViewById(R.id.recyc_Stage);
        //set adapter recyclerView
        itemStages = ItemStage.inititStage(lstname);
        adapterStage =new AdapterStage(itemStages,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyc_Stage.setLayoutManager(layoutManager);

        //MoveItem
        ItemTouchHelper.Callback callback =
                new ItemMoveStage(adapterStage);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyc_Stage);
        recyc_Stage.setAdapter(adapterStage);

        //Snap item recyclerView
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyc_Stage);
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
                        if (!stageName.isEmpty()) {
                            ItemStage itemStage = new ItemStage();
                            itemStage.tvStageName = stageName;
                            itemStages.add(itemStage);
                            adapterStage.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "New Stage Added", Toast.LENGTH_LONG).show();
                            // Handle non-empty stageName
                        } else {
                            // Handle empty stageName or display a message
                            Toast.makeText(getApplicationContext(), "Stage name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
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
        ListView listView = dialogView.findViewById(R.id.list_email);
        ListView listAavatar = dialogView.findViewById(R.id.list_avatar);

        AdapterMember adapterMember = new AdapterMember(getApplicationContext(),
                R.layout.item_email,
                project.getMembers());

        listAavatar.setAdapter(adapterMember);

        builder.setView(dialogView)
                .setPositiveButton(R.string.AddMember, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
        }

        return super.onOptionsItemSelected(item);
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