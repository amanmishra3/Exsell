package com.android.exsell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.exsell.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    EditText emailField = null;
    EditText passwordField = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
    }

    public void goToSignUp(View view) {
        Log.i(TAG, "goToSignUp");
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void loginClick(View view) {
        Log.i(TAG, "loginClick");
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (validateFields(view)) {
            signIn(email, password);
        }
    }

    public boolean validateFields(View view) {
        Log.i(TAG, "validateFields");
        boolean valid = true;
        if (emailField.getText().toString().isEmpty()) {
            emailField.setError("Email cannot be empty");
            valid = false;
        } else {
            emailField.setError(null);
        }
        if (passwordField.getText().toString().isEmpty()) {
            passwordField.setError("Password cannot be empty");
            valid = false;
        } else {
            passwordField.setError(null);
        }
        return valid;
    }

    private void signIn(String email, String password) {
        Log.i(TAG, "signIn");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Log.i(TAG, "updateUI");
        passwordField.setText(null);
        if (user != null) {
            emailField.setText(null);
            if(user.isEmailVerified()) {
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Please verify Email",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}