package com.example.harsha.newsapp;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by harsha on 30/01/17.
 */
@IgnoreExtraProperties
public class UserDetails {

    String userName;
    String password;
    String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
