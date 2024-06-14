package com.example.healthtracker.ViewModel;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtracker.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpViewModel extends ViewModel {
    private User user;
    private MutableLiveData<String> usernameErrorMessage;
    private MutableLiveData<String> passwordErrorMessage;
    private MutableLiveData<String> changedUsername;
    private MutableLiveData<String> generalErrorMessage;

    public SignUpViewModel() {
        user = new User(FirebaseAuth.getInstance());
        changedUsername = new MutableLiveData<String>(null);
        passwordErrorMessage = new MutableLiveData<String>(null);
        usernameErrorMessage = new MutableLiveData<String>(null);
        generalErrorMessage = new MutableLiveData<String>(null);
    }

    public LiveData<String> getUsernameErrorMessage() {
        return usernameErrorMessage;
    }

    public LiveData<String> getPasswordErrorMessage() {
        return passwordErrorMessage;
    }

    public LiveData<String> getGeneralErrorMessage() {
        return generalErrorMessage;
    }

    public void updateUsernameErrorMessage(String error) {
        usernameErrorMessage.setValue(error);
    }

    public void updatePasswordErrorMessage(String error) {
        passwordErrorMessage.setValue(error);
    }

    public void updateGeneralErrorMessage(String error) {
        generalErrorMessage.setValue(error);
    }

    public boolean checkUsernameAndPassword(String username, String password) {
        boolean correctCredentials = true;

        if (username == null || username.length() == 0) {
            updateUsernameErrorMessage("Username is empty");
            correctCredentials = false;
        } else if (username.trim().length() == 0) {
            updateUsernameErrorMessage("Username cannot be only whitespace");
            correctCredentials = false;
        }

        if (password == null || password.length() == 0) {
            updatePasswordErrorMessage("Password is empty");
            correctCredentials = false;
        } else if (password.trim().length() == 0) {
                updatePasswordErrorMessage("Password cannot be only whitespace");
                correctCredentials = false;
        } else if (password.trim().length() < 6) {
            updatePasswordErrorMessage("Password needs to be 6 char or longer");
            correctCredentials = false;
        }
        return correctCredentials;
    }

    public boolean validateUsername(String username) {
        return username.toLowerCase().matches("/^\\S+@\\S+\\.\\S+$/\n");
    };

    public boolean signUp(Activity signUp, String username, String password) {
        changedUsername = new MutableLiveData<String>(null);
        passwordErrorMessage = new MutableLiveData<String>(null);
        usernameErrorMessage = new MutableLiveData<String>(null);
        generalErrorMessage = new MutableLiveData<String>(null);

        final boolean[] checkFields = {checkUsernameAndPassword(username,
                password)};

        try {
            changedUsername.setValue(username);
            if (checkFields[0] && !(validateUsername(username))) {
                changedUsername.setValue(changedUsername.getValue() + "@gmail.com");
            }
            if (changedUsername.getValue() != null) {
                changedUsername.setValue(changedUsername.getValue().trim());
            }

            // Create user using Firebase Authentication
            user.getAuth().createUserWithEmailAndPassword(changedUsername.getValue(),
                            password)
                    .addOnCompleteListener(signUp, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser newUser = user.getAuth().getCurrentUser();
                            } else {
                                // Sign in failure, update error message
                                updateGeneralErrorMessage("Invalid Username/Password");
                                checkFields[0] = false;
                            }
                        }
                    });
        } catch (Exception e) {
            // Sign in failure, update error message
            updateGeneralErrorMessage("Invalid Username/Password");
            checkFields[0] = false;
        }
        return checkFields[0];
    }
}
