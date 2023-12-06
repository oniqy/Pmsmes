package com.example.pmsmes.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmsmes.Controller.ProjectActivity;
import com.example.pmsmes.Controller.ProjectWorkspaceActivity;
import com.example.pmsmes.ItemAdapter.Project;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterProject extends RecyclerView.Adapter<AdapterProject.ViewHolder> {

    Context context;
    ArrayList<Project> projects;

    public AdapterProject(Context context, ArrayList<Project> projects) {
        this.context = context;
        this.projects = projects;
    }
    APIInterface api;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        api = APIClient.getClient().create(APIInterface.class);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.projectName.setText(project.getName());
//        Resources resources = holder.itemView.getContext().getResources();
//        @SuppressLint("DiscouragedApi") int background = resources.getIdentifier(project.getBackground(), "drawable", context.getPackageName());
//        holder.projectBackground.setImageResource(background);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent projectView = new Intent(view.getContext(), ProjectWorkspaceActivity.class);
                projectView.putExtra("projectID", project.getId());
                view.getContext().startActivity(projectView);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final CharSequence[] items = {"Xóa dự án"};
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Tùy chọn");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                removeProject(view.getContext(), project.getId());
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
    }
    private void removeProject(Context context,String id){
        api.deleteProject(APIClient.getToken(context),id).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView projectBackground;
        TextView projectName;

        public ViewHolder(@NonNull View item) {
            super(item);
            projectBackground = item.findViewById(R.id.projectBackground);
            projectName = item.findViewById(R.id.projectName);
        }
    }


}
