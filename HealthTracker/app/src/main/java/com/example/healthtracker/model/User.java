package com.example.healthtracker.model;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String username;

    private volatile static User uniqueInstance;

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