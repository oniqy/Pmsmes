package com.example.pmsmes.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class Account {
    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

}