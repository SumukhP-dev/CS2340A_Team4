package com.example.healthtracker.model;

import com.google.firebase.auth.FirebaseAuth;

public class User {
    private FirebaseAuth mAuth;

    public User(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public void setAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }
}