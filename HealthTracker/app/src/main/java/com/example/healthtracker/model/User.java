package com.example.healthtracker.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private String username;

    private volatile static User uniqueInstance;

    private User() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
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

    public FirebaseFirestore getDatabase() {
        return database;
    }

    public void setDatabase(FirebaseFirestore database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}