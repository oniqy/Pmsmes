package com.example.pmsmes.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;

import javax.annotation.processing.Generated;

@Generated("jsonschema2pojo")
public class GetProjectStages {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private ArrayList<Data> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    @Generated("jsonschema2pojo")
    public class Data {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("sequence")
        @Expose
        private Integer sequence;
        @SerializedName("project")
        @Expose
        private String project;
        @SerializedName("isCancel")
        @Expose
        private Boolean isCancel;
        @SerializedName("isDone")
        @Expose
        private Boolean isDone;
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

        public Integer getSequence() {
            return sequence;
        }

        public void setSequence(Integer sequence) {
            this.sequence = sequence;
        }

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public Boolean getIsCancel() {
            return isCancel;
        }

        public void setIsCancel(Boolean isCancel) {
            this.isCancel = isCancel;
        }

        public Boolean getIsDone() {
            return isDone;
        }

        public void setIsDone(Boolean isDone) {
            this.isDone = isDone;
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