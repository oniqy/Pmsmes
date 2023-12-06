package com.example.pmsmes.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pmsmes.ItemAdapter.Item_Member;
import com.example.pmsmes.ItemAdapter.User;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterMember extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<User> memberList;
    String currentProejectID;
    private APIInterface apiServices = APIClient.getClient().create(APIInterface.class);

    boolean checkIsAdded;

    public AdapterMember(@NonNull Context context, int resource, @NonNull  ArrayList<User> item_members, String projectID, boolean isAdded) {
        super(context, resource, item_members);
        this.context = context;
        this.resource = resource;
        this.memberList = item_members;
        this.currentProejectID = projectID;
        this.checkIsAdded = isAdded;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User member = memberList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource,null);
        }
        TextView tv_email = convertView.findViewById(R.id.tv_email);
        TextView tv_hvt = convertView.findViewById(R.id.textView4);
        tv_email.setText(member.getEmail());
        tv_hvt.setText(member.getName());
        ImageView img_avatar = convertView.findViewById(R.id.img_avatar);
        Picasso.get().load("https://images.pexels.com/photos/5102561/pexels-photo-5102561.jpeg?").resize(100,100).into(img_avatar);


        if (!this.checkIsAdded){
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Add this user to project?");
                    builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            apiServices.addMemberToProject(APIClient.getToken(context),currentProejectID,member.getId())
                                    .enqueue(new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {
                                            if (response.isSuccessful()){
                                                Toast.makeText(context,"Member added successfully!",Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            Toast.makeText(context,"Đã thêm thành công!",Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    });

                    AlertDialog e  = builder.create();
                    e.show();
                }
            });
        }else {
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Are you sure to remove this member?");
                    builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            JsonObject body = new JsonObject();
                            body.addProperty("user", member.getId());
                            apiServices.removeMemberFromProject(APIClient.getToken(context),currentProejectID,body).enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    if (response.isSuccessful()){
                                        Toast.makeText(context, "Member removed successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                    APIClient.logData(response.body());
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    Toast.makeText(context, "Member remove failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog e = builder.create();
                    e.show();

                    return true;
                }
            });
        }


        return convertView;
    }
}
