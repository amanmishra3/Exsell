package com.android.exsell.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.exsell.R;
import com.android.exsell.UI.Home;
import com.android.exsell.UI.LoginActivity;
import com.android.exsell.UI.MainActivity;
import com.android.exsell.db.UserDb;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class FragmentLogin extends Fragment {
    private static final String TAG = "LoginFragment";
    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;
    private Button loginBtn;
    private TextView forgotPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailField = view.findViewById(R.id.email);
        passwordField = view.findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        loginBtn = view.findViewById(R.id.loginButton);
        forgotPassword = view.findViewById(R.id.forgotPasswordLink);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClick(v);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Clicked Forgot Password");
                EditText resetMail = new EditText(getActivity());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(getActivity());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your Email to get the password reset link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("SEND EMAIL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        if(mail.isEmpty()) {
                            Toast.makeText(getActivity(), "Error! Please enter email to get password reset link.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Password Reset link sent.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error! Failed to send password reset link." + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

                passwordResetDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
           }
        });
        return view;
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
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(getActivity(), "Authentication Successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserDb.setMyUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication Failed.",
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
                Intent intent = new Intent(getActivity(), Home.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Please verify Email",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }



}