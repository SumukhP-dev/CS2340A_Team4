package com.example.healthtracker.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.healthtracker.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpViewModel extends ViewModel {
    private User user;

    public SignUpViewModel() {
        user = new User(FirebaseAuth.getInstance());
    }

    public void checkAuth() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = user.getAuth().getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }
}
