package com.example.pmsmes.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;



public class ProjectResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("results")
    @Expose
    private Integer results;
    @SerializedName("data")
    @Expose
    private ArrayList<Project> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getResults() {
        return results;
    }

    public void setResults(Integer results) {
        this.results = results;
    }

    public ArrayList<Project> getData() {
        return data;
    }

    public void setData(ArrayList<Project> data) {
        this.data = data;
    }

}