package com.example.healthtracker.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthtracker.R;
import com.example.healthtracker.ViewModel.SignUpViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private SignUpViewModel signUpViewModel;
    private Button signUpButton;
    private Button exitButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Task<AuthResult> resultAuthState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        exitButton = findViewById(R.id.exitButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultAuthState = signUpViewModel.signUp(SignUpActivity.this,
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                if (Boolean.FALSE.equals(signUpViewModel.getErrorMessage().getValue())) {
                    if (signUpViewModel.getUsernameErrorMessage().getValue() != null) {
                        usernameEditText.setError("Error: "
                                + signUpViewModel.getUsernameErrorMessage().getValue());
                        Log.d("Error Validation",
                                signUpViewModel.getUsernameErrorMessage().getValue());
                    }
                    if (signUpViewModel.getPasswordErrorMessage().getValue() != null) {
                        passwordEditText.setError("Error: "
                                + signUpViewModel.getPasswordErrorMessage().getValue());
                        Log.d("Error Validation",
                                signUpViewModel.getPasswordErrorMessage().getValue());
                    }
                    if (signUpViewModel.getGeneralErrorMessage().getValue() != null) {
                        Toast.makeText(SignUpActivity.this,
                                signUpViewModel.getGeneralErrorMessage().getValue(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("Error Validation",
                                signUpViewModel.getGeneralErrorMessage().getValue());
                    }
                } else if (resultAuthState.getException() != null) {
                    Toast.makeText(SignUpActivity.this,
                            "Invalid username/password",
                            Toast.LENGTH_SHORT).show();
                    Log.d("Error Validation", "test");
                } else {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
            }
        });
    }
}