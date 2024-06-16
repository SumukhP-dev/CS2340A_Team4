package com.example.healthtracker.ViewModel;

import android.app.Activity;


import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtracker.model.User;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LoginViewModel extends ViewModel {
    private final User user;
    private MutableLiveData<String> generalErrorMessage;

    private MutableLiveData<String> formattedUsername;

    public LiveData<String> getGeneralErrorMessage() {
        return generalErrorMessage;
    }

    public void setGeneralErrorMessage(String error) {
        generalErrorMessage.setValue(error);
    }

    public LoginViewModel() {
        user = new User(FirebaseAuth.getInstance());
        generalErrorMessage = new MutableLiveData<>(null);
        formattedUsername = new MutableLiveData<String>(null);
    }

    public boolean login(Activity loginActivity, String username, String password) {
        formattedUsername = new MutableLiveData<String>(null);
        generalErrorMessage = new MutableLiveData<>(null);

        final boolean[] checkFields = {checkUsernameAndPassword(username,
                password)};

        try {
            formattedUsername.setValue(username);
            if (checkFields[0] && !(validateUsername(username))) {
                formattedUsername.setValue(formattedUsername.getValue() + "@gmail.com");
            }

            // Trims username to prevent errors in authentication
            if (formattedUsername.getValue() != null) {
                formattedUsername.setValue(formattedUsername.getValue().trim());
            }

            user.getAuth().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser signedInUser = user.getAuth().getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            setGeneralErrorMessage("Invalid username/password");
                            checkFields[0] = false;
                        }
                    }
                });
        } catch (Exception e) {
            // Sign in failure, update error message
            setGeneralErrorMessage("Invalid username/password");
            checkFields[0] = false;
        }
        return checkFields[0];
    }

    /* adapted logic from SignUpViewModel, with generalized error messages for security purposes for login functionality
    /  Sets error messages based on passed in username and password params
    */
    public boolean checkUsernameAndPassword(String username, String password) {
        boolean correctCredentials = true;

        if (username == null || username.length() == 0) {
            setGeneralErrorMessage("Invalid username");
            correctCredentials = false;
        } else if (username.trim().length() == 0) {
            setGeneralErrorMessage("Invalid username");
            correctCredentials = false;
        }

        if (password == null || password.length() == 0) {
            setGeneralErrorMessage("Invalid password");
            correctCredentials = false;
        } else if (password.trim().length() == 0) {
            setGeneralErrorMessage("Invalid password");
            correctCredentials = false;
        } else if (password.trim().length() < 6) {
            setGeneralErrorMessage("Invalid password");
            correctCredentials = false;
        }
        return correctCredentials;
    }

    // Adapted logic
    // Returns true if the username passed in is a valid gmail account
    public boolean validateUsername(String username) {
        return username.toLowerCase().matches("/^\\S+@\\S+\\.\\S+$/\n");
    };
}