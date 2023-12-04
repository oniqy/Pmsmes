package com.example.pmsmes.ItemAdapter;

public class Item_Report {
    String info;
    int count_rs;

    public Item_Report(String info, int count_rs) {
        this.info = info;
        this.count_rs = count_rs;
    }

    public Item_Report() {

    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCount_rs() {
        return count_rs;
    }

    public void setCount_rs(int count_rs) {
        this.count_rs = count_rs;
    }
}
