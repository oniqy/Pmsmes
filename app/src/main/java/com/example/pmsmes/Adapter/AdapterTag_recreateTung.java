package com.example.pmsmes.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
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

import com.example.pmsmes.ItemAdapter.Tag;
import com.example.pmsmes.R;
import com.example.pmsmes.Utils.APIClient;
import com.example.pmsmes.Utils.APIInterface;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterTag_recreateTung extends ArrayAdapter {
    Context context;

    int resource;

    ArrayList<Tag> tagAL;

    APIInterface apiInterface;

    public AdapterTag_recreateTung(@NonNull Context context, int resource, @NonNull ArrayList<Tag> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.tagAL = objects;
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @SuppressLint({"ResourceAsColor", "DiscouragedApi"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tag tag = tagAL.get(position);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(resource, null);

        }
        TextView tv = convertView.findViewById(R.id.tagName);
        tv.setText(tag.getName());
        ImageView im = convertView.findViewById(R.id.tagColor);
        im.setBackgroundColor(AppCompatResources.getColorStateList(context.getApplicationContext(), context.getResources().getIdentifier(tag.getColor(), "color", context.getPackageName())).getDefaultColor());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Edit tag");
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view1 = inflater.inflate(R.layout.row_spinner, null);
                String[] maMau = new String[]{"danger", "primary", "success", "warning"};

                EditText name = (EditText) view1.findViewById(R.id.editText1);
                name.setText(tag.getName());

                Spinner spinner = (Spinner) view1.findViewById(R.id.spinner1);


                ArrayAdapter adapter = new ArrayAdapter(context, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, maMau);

                spinner.setAdapter(adapter);
                for(int i = 0; i < maMau.length; i++){
                    if(tag.getColor().equals(maMau[i])){
                        spinner.setSelection(i);
                    }
                }
                builder.setView(view1);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JsonObject update = new JsonObject();
                        update.addProperty("tag", tag.getId());

                        update.addProperty("name", String.valueOf(tag.getName()));

                        update.addProperty("color", maMau[spinner.getSelectedItemPosition()]);
                        update.addProperty("project", tag.getProject());


                        apiInterface.updateProjectTag(APIClient.getToken(context), tag.getProject(), update).enqueue(new Callback<Object>() {

                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                if(response.isSuccessful()) {
                                    Toast.makeText(context, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                                    tagAL.set(position, tag);
                                    dialogInterface.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {
                                Toast.makeText(context, "Cap nhat khong thanh cong", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();

                            }
                        });
                    }
                });


            }

        }

        );



        return convertView;
    }
}
