package com.example.pmsmes.ItemAdapter;
import android.widget.ListView;

import java.util.ArrayList;
public class ItemStage {
    public String tvStageName;

    public String getTvStageName() {
        return tvStageName;
    }

    public void setTvStageName(String tvStageName) {
        this.tvStageName = tvStageName;
    }

    public ItemStage(String tvStageName) {
        this.tvStageName = tvStageName;
    }
    public ItemStage() {

    }

    public static ArrayList<ItemStage> inititStage(ArrayList<String> ten){
        ArrayList<ItemStage> itemStages = new ArrayList<>();
        for(int i = 0 ; i< ten.size();i++){
            ItemStage item = new ItemStage(ten.get(i));
            itemStages.add(item);
        }
        return itemStages;
    }
}
