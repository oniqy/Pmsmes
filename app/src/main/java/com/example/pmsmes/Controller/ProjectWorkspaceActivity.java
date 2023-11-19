package com.example.pmsmes.Controller;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pmsmes.Adapter.AdapterStage;
import com.example.pmsmes.Adapter.AdapterTask;
import com.example.pmsmes.ItemAdapter.ItemStage;
import com.example.pmsmes.ItemAdapter.Item_Task;
import com.example.pmsmes.R;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;

public class ProjectWorkspaceActivity extends AppCompatActivity {
    RecyclerView recyc_Stage;
    ArrayList<ItemStage> itemStages = new ArrayList<>();
    AdapterStage adapterStage;
    ImageButton img_buttonOption;
    String stageName;
    ArrayList<String> lstStage = new ArrayList<>();
    String[] lstname=new String[]{"ToDo","OnGoing","Done","Cancel"};
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_workspace2);
        recyc_Stage =(RecyclerView) findViewById(R.id.recyc_Stage);
        img_buttonOption = (ImageButton) findViewById(R.id.img_buttonOption);
        itemStages = ItemStage.inititStage(lstname);
        adapterStage =new AdapterStage(itemStages,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyc_Stage.setLayoutManager(layoutManager);
        recyc_Stage.setAdapter(adapterStage);
        img_buttonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsMenu(v);
            }
        });
    }

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
                .setPositiveButton(R.string.Add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        stageName = edt_stageName.getText().toString().trim();
                        if (!stageName.isEmpty()) {
                            ItemStage itemStage = new ItemStage();
                            itemStage.tvStageName = stageName;
                            itemStages.add(itemStage);
                            adapterStage = new AdapterStage(itemStages,getApplicationContext());
                            recyc_Stage.setAdapter(adapterStage);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_item_addMember) {

            Toast.makeText(getApplicationContext(),"Add member",Toast.LENGTH_LONG).show();
            return true;
        } else if (itemId == R.id.menu_item_addStage) {
            showAddStageDialog();
            Toast.makeText(getApplicationContext(),"Add Stage",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}