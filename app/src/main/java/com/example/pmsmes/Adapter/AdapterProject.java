package com.example.pmsmes.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pmsmes.ItemAdapter.Project;
import com.example.pmsmes.R;

import java.util.ArrayList;

public class AdapterProject extends RecyclerView.Adapter<AdapterProject.ViewHolder> {

    Context context;
    ArrayList<Project> projects;

    public AdapterProject(Context context, ArrayList<Project> projects) {
        this.context = context;
        this.projects = projects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.projectName.setText(project.getName());
        Resources resources = holder.itemView.getContext().getResources();
        @SuppressLint("DiscouragedApi") int background = resources.getIdentifier(project.getBackground(), "drawable", context.getPackageName());
        holder.projectBackground.setImageResource(background);
    }

    @Override
    public int getItemCount() {
        return projects != null ? projects.size() : 0;
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
