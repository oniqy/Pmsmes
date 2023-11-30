package com.example.pmsmes.ItemAdapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class Task {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("creator")
    @Expose
    private User creator;
    @SerializedName("assignee")
    @Expose
    private ArrayList<User> assignee;
    @SerializedName("stage")
    @Expose
    private String stage;
    @SerializedName("tags")
    @Expose
    private ArrayList<Tag> tags;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public ArrayList<User> getAssignee() {
        return assignee;
    }

    public void setAssignee(ArrayList<User> assignee) {
        this.assignee = assignee;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag>tags) {
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}