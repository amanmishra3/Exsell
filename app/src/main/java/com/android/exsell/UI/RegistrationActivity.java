package com.android.exsell.UI;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.exsell.R;
import com.android.exsell.db.UserDb;
import com.android.exsell.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    private FirebaseAuth mAuth;
    private UserDb userDb;
    EditText nameField = null;
    EditText schoolField = null;
    EditText emailField = null;
    EditText passwordField = null;
    EditText password2Field = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userDb = UserDb.newInstance();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        nameField = findViewById(R.id.name);
        schoolField = findViewById(R.id.school);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        password2Field = findViewById(R.id.password2);
    }

    public void goToSignIn(View view) {
        Log.i(TAG, "goToSignIn");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void createClick(View view) {
        Log.i(TAG, "createClick");

        if (validateFields()) {
            String fname = nameField.getText().toString();
            String school = schoolField.getText().toString();
            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            Users person = new Users(fname, school, email, null);
            createAccount(person, password);
        }
    }

    public boolean validateFields() {
        Log.i(TAG, "validateFields");
        boolean valid = true;
        if (nameField.getText().toString().isEmpty()) {
            nameField.setError("Name cannot be empty");
            valid = false;
        } else {
            nameField.setError(null);
        }
        if (schoolField.getText().toString().isEmpty()) {
            schoolField.setError("School cannot be empty");
            valid = false;
        } else {
            schoolField.setError(null);
        }
        if (emailField.getText().toString().isEmpty()) {
            emailField.setError("Email cannot be empty");
            valid = false;
        } else if (!validateEmail(emailField.getText().toString())) {
            emailField.setError("Email address is not valid for santa clara");
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
        if (password2Field.getText().toString().isEmpty()) {
            password2Field.setError("Password cannot be empty");
            valid = false;
        } else if (!passwordField.getText().toString().equals(password2Field.getText().toString())) {
            password2Field.setError("Passwords don't match");
            valid = false;
        } else {
            password2Field.setError(null);
        }
        return valid;
    }


    public boolean validateEmail(String email) {
        Log.i(TAG, "validateEmail");
        if (email.length() < 9)
            return false;
        String[] comp = email.split("@", 2);
        return comp[1].equals("scu.edu");
    }

    private void createAccount(Users user, String password) {
        Log.i(TAG, "createAccount");
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            makeText(RegistrationActivity.this, "Account Created",
                                    LENGTH_SHORT).show();
                            sendEmailVerification();
                            FirebaseUser myUser = mAuth.getCurrentUser();
                            assert user != null;
                            user.setUserId(myUser.getUid());
                            userDb.createDocument(user);
                            updateUI(myUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            makeText(RegistrationActivity.this, task.getException().getMessage(),
                                    LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void sendEmailVerification() {
        Log.i(TAG, "sendEmailVerification");
        // Send verification email
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                        makeText(RegistrationActivity.this, "Verification email sent",
                                LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Log.i(TAG, "updateUI");
        if (user != null) {
            nameField.setText(null);
            schoolField.setText(null);
            emailField.setText(null);
            passwordField.setText(null);
            password2Field.setText(null);
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }
    }
}
