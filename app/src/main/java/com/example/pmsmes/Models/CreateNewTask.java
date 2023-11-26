package com.example.pmsmes.Models;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Generated("jsonschema2pojo")
public class CreateNewTask {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    @Generated("jsonschema2pojo")
    public class Data {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("creator")
        @Expose
        private String creator;
        @SerializedName("assignee")
        @Expose
        private ArrayList<Object> assignee;
        @SerializedName("stage")
        @Expose
        private String stage;
        @SerializedName("tags")
        @Expose
        private ArrayList<Object> tags;
        @SerializedName("__v")
        @Expose
        private Integer v;
        @SerializedName("id")
        @Expose
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public ArrayList<Object> getAssignee() {
            return assignee;
        }

        public void setAssignee(ArrayList<Object> assignee) {
            this.assignee = assignee;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public ArrayList<Object> getTags() {
            return tags;
        }

        public void setTags(ArrayList<Object> tags) {
            this.tags = tags;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}