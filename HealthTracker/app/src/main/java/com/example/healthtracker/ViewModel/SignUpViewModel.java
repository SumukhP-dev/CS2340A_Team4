package com.example.healthtracker.ViewModel;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.healthtracker.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpViewModel extends ViewModel {
    private User user;
    private MutableLiveData<String> usernameErrorMessage;
    private MutableLiveData<String> passwordErrorMessage;
    private MutableLiveData<String> changedUsername;
    private MutableLiveData<String> generalErrorMessage;
    private MutableLiveData<Boolean> errorMessage;

    public SignUpViewModel() {
        user = new User(FirebaseAuth.getInstance());
        changedUsername = new MutableLiveData<String>(null);
        passwordErrorMessage = new MutableLiveData<String>(null);
        usernameErrorMessage = new MutableLiveData<String>(null);
        generalErrorMessage = new MutableLiveData<String>(null);
        errorMessage = new MutableLiveData<Boolean>(null);
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

    public LiveData<Boolean> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(boolean statusMessage) {
        this.errorMessage.setValue(statusMessage);
    }

    // Sets error messages based on passed in username and password params
    public void checkUsernameAndPassword(String username, String password) {
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
        setErrorMessage(correctCredentials);
    }

    // Returns true if the username passed in is a valid gmail account
    public boolean validateUsername(String username) {
        return username.toLowerCase().length() > 10 && username.toLowerCase()
                .substring(username.length() - 10).equals("@gmail.com");
    };

    // Creates a user using Firebase Authentication
    public Task<AuthResult> signUp(String username, String password) {
        changedUsername = new MutableLiveData<String>(null);
        passwordErrorMessage = new MutableLiveData<String>(null);
        usernameErrorMessage = new MutableLiveData<String>(null);
        generalErrorMessage = new MutableLiveData<String>(null);
        errorMessage = new MutableLiveData<Boolean>(null);

        checkUsernameAndPassword(username, password);
        changedUsername.setValue(username);
            if (!(validateUsername(username))) {
                changedUsername.setValue(changedUsername.getValue() + "@gmail.com");
            }

            // Trims username to prevent errors in authentication
            if (changedUsername.getValue() != null) {
                changedUsername.setValue(changedUsername.getValue().trim());
            }

            if (Boolean.FALSE.equals(errorMessage.getValue())) {
                return null;
            }

            return user.getAuth().createUserWithEmailAndPassword(changedUsername.getValue(),
                            password);
    }
}
