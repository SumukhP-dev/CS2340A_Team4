package com.example.healthtracker.view;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.healthtracker.R;
import com.example.healthtracker.ViewModel.LoginViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;
    private Button loginButton;
    private Button signUpButton;
    private Button exitButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Task<AuthResult> resultAuthState;

    private boolean checkAuthState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginButton = findViewById(R.id.logInButton);
        signUpButton = findViewById(R.id.signUpButton);
        exitButton = findViewById(R.id.exitButton);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultAuthState = loginViewModel.login(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                if (Boolean.FALSE.equals(loginViewModel.getErrorMessage().getValue())) {
                    if (loginViewModel.getUsernameErrorMessage().getValue() != null) {
                        usernameEditText.setError("Error: "
                                + loginViewModel.getUsernameErrorMessage().getValue());
                        Log.d("Error Validation",
                                loginViewModel.getUsernameErrorMessage().getValue());
                    }
                    if (loginViewModel.getPasswordErrorMessage().getValue() != null) {
                        passwordEditText.setError("Error: "
                                + loginViewModel.getPasswordErrorMessage().getValue());
                        Log.d("Error Validation",
                                loginViewModel.getPasswordErrorMessage().getValue());
                    }
                    if (loginViewModel.getGeneralErrorMessage().getValue() != null) {
                        Toast.makeText(LoginActivity.this,
                                loginViewModel.getGeneralErrorMessage().getValue(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("Error Validation",
                                loginViewModel.getGeneralErrorMessage().getValue());
                    }
                } else {
                    resultAuthState.addOnCompleteListener(LoginActivity.this,
                            new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(LoginActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this,
                                            "Invalid username/password",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
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