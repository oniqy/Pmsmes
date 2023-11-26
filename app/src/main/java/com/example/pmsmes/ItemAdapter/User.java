package com.example.pmsmes.ItemAdapter;

import java.util.Date;

public class User {
    String id, name, email;
    Date birthday;
    Account account;

    public User() {
    }

    public User(String id, String name, String email, Date birthday, Account account) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.account = account;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
