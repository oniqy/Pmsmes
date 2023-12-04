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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pmsmes.ItemAdapter.Item_Member;
import com.example.pmsmes.ItemAdapter.User;
import com.example.pmsmes.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterMember extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<User> memberList;
    public AdapterMember(@NonNull Context context, int resource, @NonNull  ArrayList<User> item_members) {
        super(context, resource, item_members);
        this.context = context;
        this.resource = resource;
        this.memberList = item_members;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User member = memberList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource,null);
        }
        TextView tv_email = convertView.findViewById(R.id.tv_email);
        tv_email.setText(member.getEmail());
        ImageView img_avatar = convertView.findViewById(R.id.img_avatar);
        Picasso.get().load("https://images.pexels.com/photos/5102561/pexels-photo-5102561.jpeg?").resize(100,100).into(img_avatar);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add this user to project?");
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
            }
        });
        return convertView;
    }
}
