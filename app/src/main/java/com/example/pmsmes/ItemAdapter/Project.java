package com.example.pmsmes.ItemAdapter;

import java.util.ArrayList;
import java.util.Date;

public class Project {
    private String id, name, description, background;
    private Date startAt, endAt;
    private User creator;
    private ArrayList<User> members;

    public Project() {
    }

    public Project(String id, String name, String description, String background, Date startAt, Date endAt, User creator, ArrayList<User> members) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.background = background;
        this.startAt = startAt;
        this.endAt = endAt;
        this.creator = creator;
        this.members = members;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
