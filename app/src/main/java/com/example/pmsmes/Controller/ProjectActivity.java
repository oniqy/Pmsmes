package com.example.pmsmes.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
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
import java.util.Objects;

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
    String token;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        mapWidget();
        showDialog();
        getAllProject();
    }

    private void mapWidget() {
        title_textView = findViewById(R.id.title_textView);
        img_buttonOption = (ImageButton) findViewById(R.id.img_buttonOption);
        projectRecycles = findViewById(R.id.projectRecycles);
        projectPlus = findViewById(R.id.projectPlus);
        adapterProject = new AdapterProject(getApplicationContext(), projects);
        api = APIClient.getClient().create(APIInterface.class);
        token = APIClient.getToken(getApplicationContext());
        userID = APIClient.getUserID(getApplicationContext());
        projectRecycles.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int index = projectRecycles.getId();
                Project project = projects.get(index);
                Log.i("tuan",project.getName());
                final CharSequence[] items = {"Xóa dự án"};
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Tùy chọn");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("Yeahhhhhhhhhhhhhhhhhhhhh");
                        removeProject(project.getId());
                    }
                });
                builder.show();
                return true;
            }
        });
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
    public void removeProject(String id){
        assert !Objects.equals(token, "Bearer ");
        api.deleteProject(token,id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (response.isSuccessful()){
                    projectRecycles.removeAllViews();
                    adapterProject.notifyDataSetChanged();
                    projects.clear();
                    getAllProject();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Có gì đó sai sai! Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllProject() {
        assert !Objects.equals(token, "Bearer ");
        assert userID != null;
        api.getMyProject(token, userID).enqueue(new Callback<Object>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String strObj = gson.toJson(response.body());
                projects.clear();
                try {
                    JSONObject arrObjects = new JSONObject(strObj);
                    for (int i = 0; i < arrObjects.getJSONArray("data").length(); i++) {
                        Project project = new Project();
                        JSONObject data = arrObjects.getJSONArray("data").getJSONObject(i);
                        project.setName(data.getString("name"));
                        project.setDescription(data.getString("description"));
                        project.setId(data.getString("id"));
                        projects.add(project);
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

    private void createProject(String name) {
        assert !Objects.equals(token, "Bearer ");
        api.createNewProject(token, name, "", APIClient.getUserID(getApplicationContext())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    getAllProject();
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Có gì đó sai sai! Vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}