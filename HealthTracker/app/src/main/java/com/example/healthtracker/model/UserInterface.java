package com.example.healthtracker.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public interface UserInterface {
    FirebaseAuth getAuth();
    void setAuth(FirebaseAuth mAuth);
    FirebaseDatabase getDatabase();
    void setDatabase(FirebaseDatabase database);
    String getUsername();
    void setUsername(String username);
}
