package com.example.pmsmes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pmsmes.ItemAdapter.Item_Member;
import com.example.pmsmes.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterMember extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<Item_Member> item_members;
    public AdapterMember(@NonNull Context context, int resource, @NonNull  ArrayList<Item_Member> item_members) {
        super(context, resource, item_members);
        this.context = context;
        this.resource = resource;
        this.item_members = item_members;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Item_Member item_member = new Item_Member();
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource,null);
        }
        TextView tv_email = convertView.findViewById(R.id.tv_email);
        tv_email.setText(item_member.getEmail());
        ImageView img_avatar = convertView.findViewById(R.id.img_avatar);
        Picasso.with(context).load(item_member.getAvata()).
                resize(100,100).into(img_avatar);
        return convertView;
    }
}
