package com.example.pmsmes.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.pmsmes.Controller.ProjectWorkspaceActivity;
import com.example.pmsmes.ItemAdapter.Tag;
import com.example.pmsmes.ItemAdapter.User;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterTag extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<Tag> tagArrayList;
    private APIInterface apiServices;
    public AdapterTag(@NonNull Context context, int resource, @NonNull  ArrayList<Tag> item_members) {
        super(context, resource, item_members);
        this.context = context;
        this.resource = resource;
        this.tagArrayList = item_members;
        apiServices = APIClient.getClient().create(APIInterface.class);

    }

    @SuppressLint({"ResourceAsColor", "DiscouragedApi"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tag tag = tagArrayList.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource,null);
        }
        TextView tv_email = convertView.findViewById(R.id.tagName);
        tv_email.setText(tag.getName());
        ImageView img_Color = convertView.findViewById(R.id.tagColor);
        img_Color.setBackgroundColor(AppCompatResources.getColorStateList(context.getApplicationContext(), context.getResources().getIdentifier(tag.getColor(),"color",context.getPackageName())).getDefaultColor());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Edit tag");
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.row_spinner,null);
                String[] colorCode = new String[]{"danger", "primary","success", "warning"};


                EditText tagName = (EditText) layout.findViewById(R.id.editText1);
                tagName.setText(tag.getName());

                Spinner s = (Spinner) layout.findViewById(R.id.spinner1);

                ArrayAdapter adapter = new ArrayAdapter(context, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, colorCode);

                s.setAdapter(adapter);
                for (int i =0; i<colorCode.length; i++){

                    if (tag.getColor().equals(colorCode[i]))
                        s.setSelection(i);
                }
                builder.setView(layout);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       //Save tag here
                        JsonObject tagUpdate = new JsonObject();
                        tagUpdate.addProperty("tag", tag.getId());
                        if (!TextUtils.isEmpty(tagName.getText()))
                            tagUpdate.addProperty("name", String.valueOf(tagName.getText()));

                        tagUpdate.addProperty("color", colorCode[s.getSelectedItemPosition()]);
                        tagUpdate.addProperty("project", tag.getProject());

                        Log.d("AKI", colorCode[s.getSelectedItemPosition()] );
                        apiServices.updateProjectTag(APIClient.getToken(context),tag.getProject(), tagUpdate)
                                .enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        if (response.isSuccessful()){
                                            Toast.makeText(context,"Tag updated", Toast.LENGTH_SHORT).show();
                                            tagArrayList.set(position,tag);
                                            dialogInterface.dismiss();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                        Toast.makeText(context,"Tag update failed", Toast.LENGTH_SHORT).show();
                                        dialogInterface.dismiss();
                                    }
                                });

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
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        apiServices.removeProjectTag(APIClient.getToken(context), tagArrayList.get(i).getProject(),tagArrayList.get(i).getId())
                                .enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(context,"Tag deleted", Toast.LENGTH_SHORT).show();
                                    tagArrayList.remove(position);
                                    dialogInterface.dismiss();


                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                Toast.makeText(context,"Tag delete failed", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        });
                    }
                });



                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog a = builder.create();
                a.show();

                return true;
            }
        });

        return convertView;
    }
}
