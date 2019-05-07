package com.lamtt.learnenglish.object;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class User {


    private String email;
    private String name;



    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User() {
    }



    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
