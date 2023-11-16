package com.example.pmsmes.ItemAdapter;
import android.widget.ListView;

import java.util.ArrayList;
public class ItemStage {
    private String tvStageName;

    public String getTvStageName() {
        return tvStageName;
    }

    public void setTvStageName(String tvStageName) {
        this.tvStageName = tvStageName;
    }

    public ItemStage(String tvStageName) {
        this.tvStageName = tvStageName;
    }

    public static ArrayList<ItemStage> inititStage(String[] ten){
        ArrayList<ItemStage> itemStages = new ArrayList<>();
        for(int i = 0 ; i< ten.length;i++){
            ItemStage item = new ItemStage(ten[i]);
            itemStages.add(item);
        }
        return itemStages;
    }
}
