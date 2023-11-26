package com.example.pmsmes.Models;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Generated("jsonschema2pojo")
public class GetProjectTask {

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

        @Generated("jsonschema2pojo")
        public class Assignee {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("id")
            @Expose
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

        }
        @Generated("jsonschema2pojo")
        public class Creator {

            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("id")
            @Expose
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

        }
        @SerializedName("tags")
        @Expose
        private ArrayList<Object> tags;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("creator")
        @Expose
        private Creator creator;
        @SerializedName("stage")
        @Expose
        private Object stage;
        @SerializedName("__v")
        @Expose
        private Integer v;
        @SerializedName("assignee")
        @Expose
        private ArrayList<Assignee> assignee;
        @SerializedName("id")
        @Expose
        private String id;

        public ArrayList<Object> getTags() {
            return tags;
        }

        public void setTags(ArrayList<Object> tags) {
            this.tags = tags;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Creator getCreator() {
            return creator;
        }

        public void setCreator(Creator creator) {
            this.creator = creator;
        }

        public Object getStage() {
            return stage;
        }

        public void setStage(Object stage) {
            this.stage = stage;
        }

        public Integer getV() {
            return v;
        }

        public void setV(Integer v) {
            this.v = v;
        }

        public ArrayList<Assignee> getAssignee() {
            return assignee;
        }

        public void setAssignee(ArrayList<Assignee> assignee) {
            this.assignee = assignee;
        }

        public String getId() {
            return id;
        }


    }

}