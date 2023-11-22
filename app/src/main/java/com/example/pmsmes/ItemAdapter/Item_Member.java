package com.example.pmsmes.ItemAdapter;

import android.widget.ImageView;

public class Item_Member {
    String avata;
    String email;

    public Item_Member() {

    }

    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Item_Member(String avata, String email) {
        this.avata = avata;
        this.email = email;
    }

}
