package com.example.healthtracker.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String username;

    private static volatile User uniqueInstance;

    private User() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        username = "";
    }

    public static User getInstance() {
        if (uniqueInstance == null) {
            synchronized (User.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new User();
                }
            }
        }
        return uniqueInstance;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public void setAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}