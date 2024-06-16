package com.example.healthtracker.ViewModel;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
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
    private MutableLiveData<Boolean> loginSuccess;

    public LiveData<String> getGeneralErrorMessage() {
        return generalErrorMessage;
    }

    public void setGeneralErrorMessage(String error) {
        generalErrorMessage.setValue(error);
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LoginViewModel() {
        user = new User(FirebaseAuth.getInstance());
        generalErrorMessage = new MutableLiveData<>(null);
        loginSuccess = new MutableLiveData<>(false);
    }

    public void login(Activity loginActivity, String username, String password) {
        generalErrorMessage.setValue(null);
        final boolean[] checkFields = {checkUsernameAndPassword(username, password)};

        if (checkFields[0]) {
            if (!validateUsername(username)) {
                username += "@gmail.com";
            }
        username = username.trim();

            user.getAuth().signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser signedInUser = user.getAuth().getCurrentUser();
                                if (signedInUser != null) {
                                    loginSuccess.setValue(true);
                                } else {
                                    setGeneralErrorMessage("Invalid username/password");
                                    loginSuccess.setValue(false);
                                }
                            } else {
                                setGeneralErrorMessage("Invalid username/password");
                                loginSuccess.setValue(false);
                            }
                        }
                    });
        } else {
            setGeneralErrorMessage("Invalid username/password");
            loginSuccess.setValue(false);
        }
    }

    public boolean checkUsernameAndPassword(String username, String password) {
        boolean correctCredentials = true;

        if (username == null || username.length() == 0) {
            setGeneralErrorMessage("Invalid username/password");
            correctCredentials = false;
        } else if (username.trim().length() == 0) {
            setGeneralErrorMessage("Invalid username/password");
            correctCredentials = false;
        }

        if (password == null || password.length() == 0) {
            setGeneralErrorMessage("Invalid username/password");
            correctCredentials = false;
        } else if (password.trim().length() == 0) {
            setGeneralErrorMessage("Invalid username/password");
            correctCredentials = false;
        } else if (password.trim().length() < 6) {
            setGeneralErrorMessage("Invalid username/password");
            correctCredentials = false;
        }
        return correctCredentials;
    }

    public boolean validateUsername(String username) {
        return username.toLowerCase().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }
}
