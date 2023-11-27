package com.example.pmsmes.Controller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmsmes.Adapter.AdapterProject;
import com.example.pmsmes.ItemAdapter.Project;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

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
    Dialog dialog;
    APIInterface api;

    TextView title_textView;
    ImageButton img_buttonOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        mapWidget();
        showDialog();
        getAllProject();
        //dummy();



    }

    private void mapWidget() {
        title_textView = findViewById(R.id.title_textView);
        img_buttonOption = (ImageButton) findViewById(R.id.img_buttonOption);
        projectRecycles = findViewById(R.id.projectRecycles);
        projectPlus = findViewById(R.id.projectPlus);
        adapterProject = new AdapterProject(getApplicationContext(), projects);
        api = APIClient.getClient().create(APIInterface.class);
    }

    private void showDialog() {

        title_textView.setText(APIClient.getLoggedinName(getApplicationContext()) + "'s workspace");

        projectPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(view.getContext());
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setContentView(R.layout.dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.Base_Theme_Pmsmes;
                EditText dialogProjectName = dialog.findViewById(R.id.dialogProjectName);
                Button dialogCreate = dialog.findViewById(R.id.dialogCreate);
                Button dialogCancel = dialog.findViewById(R.id.dialogCancel);
                disableButton(dialogProjectName, dialogCreate);
                dialogCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Create project
                        createProject(dialogProjectName.getText().toString());
                        dialog.dismiss();
                    }
                });
                dialogCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        img_buttonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionsMenu(view);
            }
        });
    }

    //show option Menu
    @SuppressLint("ResourceType")
    private void showOptionsMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_project, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onOptionsItemSelected(item);
                return true;
            }
        });
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_logout) {
            logOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logOut(){
        api.logout(APIClient.getToken(getApplicationContext())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    APIClient.logOut(getApplicationContext());
                    finish();
                    Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginActivity);
                }

            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Có gì đó sai sai! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void disableButton(EditText edit, Button button) {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("")) {
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                button.setEnabled(!TextUtils.isEmpty(charSequence.toString().trim()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getAllProject() {
        String userID = APIClient.getUserID(getApplicationContext());
        String token = APIClient.getToken(getApplicationContext());
        if (userID == null || token == "Bearer ") return;
        api.getMyProject(token, userID).enqueue(new Callback<Object>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String strObj = gson.toJson(response.body());
                ArrayList<Project> projects1 = new ArrayList<>();
                try {
                    JSONObject arrObjects = new JSONObject(strObj);
                    for (int i = 0; i < arrObjects.getJSONArray("data").length(); i++) {
                        Project project = new Project();
                        JSONObject data = arrObjects.getJSONArray("data").getJSONObject(i);
                        project.setName(data.getString("name"));
                        project.setDescription(data.getString("description"));
                        project.setId(data.getString("id"));
                        projects1.add(project);
                        projects.clear();
                        projects.addAll(projects1);
                        adapterProject = new AdapterProject(getApplicationContext(),projects);
                        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
                        projectRecycles.setLayoutManager(manager);
                        projectRecycles.setAdapter(adapterProject);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Có gì đó sai sai! Xin vui lòng thử lại", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void dummy() {
        for (int i = 1; i <= 10; i++) {
            Project project = new Project();
            project.setName(String.format("Dự án %s", i));
            projects.add(project);
        }
        adapterProject = new AdapterProject(getApplicationContext(),projects);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(),2,LinearLayoutManager.VERTICAL,false);
        projectRecycles.setLayoutManager(manager);
        projectRecycles.setAdapter(adapterProject);
    }

    private void createProject(String name) {

        api.createNewProject(APIClient.getToken(getApplicationContext()), name, "", APIClient.getUserID(getApplicationContext())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                APIClient.logData(response.body());
                if (response.isSuccessful()){
                    getAllProject();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Có gì đó sai sai! Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                Log.d("aki", t.getMessage());
            }
        });

    }


}