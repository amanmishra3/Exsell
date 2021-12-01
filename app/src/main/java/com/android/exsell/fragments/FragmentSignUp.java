package com.android.exsell.fragments;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.exsell.R;
import com.android.exsell.UI.Home;
import com.android.exsell.UI.LoginActivity;
import com.android.exsell.UI.MainActivity;
import com.android.exsell.UI.RegistrationActivity;
import com.android.exsell.db.UserDb;
import com.android.exsell.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentSignUp extends Fragment {
    private static final String TAG = "RegistrationActivity";
    private FirebaseAuth mAuth;
    private UserDb userDb;
    EditText nameField = null;
    EditText schoolField = null;
    EditText emailField = null;
    EditText passwordField = null;
    EditText password2Field = null;
    Button signUpBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        userDb = UserDb.newInstance();
        mAuth = FirebaseAuth.getInstance();
        nameField = view.findViewById(R.id.name);
        schoolField = view.findViewById(R.id.school);
        emailField = view.findViewById(R.id.email);
        passwordField = view.findViewById(R.id.password);
        password2Field = view.findViewById(R.id.password2);
        signUpBtn = view.findViewById(R.id.createAccountBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClick(v);
            }
        });
        return view;
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
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getActivity(), "Account Created",
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
                            Toast.makeText(getActivity(), task.getException().getMessage(),
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
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                        Toast.makeText(getActivity(), "Verification email sent",
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
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }
}