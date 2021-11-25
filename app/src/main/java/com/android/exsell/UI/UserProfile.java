package com.android.exsell.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.exsell.R;

public class UserProfile extends AppCompatActivity {

    ImageView editProfile, addImage;
    Button updateProfile;
    TextView userName, userEmail, userPhone, userDob, userRating;
    LinearLayout linearEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        editProfile = findViewById(R.id.editProfile);
        updateProfile = findViewById(R.id.profileUpdate);
        addImage = findViewById(R.id.addImage);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPhone = findViewById(R.id.userPhone);
        userDob = findViewById(R.id.userDOB);
        userRating = findViewById(R.id.userRating);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile.setVisibility(View.INVISIBLE);
                updateProfile.setVisibility(View.VISIBLE);
                addImage.setVisibility(View.VISIBLE);

                userName.setCursorVisible(true);
                userName.setFocusable(true);
                userName.setEnabled(true);
                userName.setClickable(true);
                userName.setFocusableInTouchMode(true);
                userName.setInputType(InputType.TYPE_CLASS_TEXT);
                userName.requestFocus();

                userPhone.setCursorVisible(true);
                userPhone.setFocusable(true);
                userPhone.setEnabled(true);
                userPhone.setClickable(true);
                userPhone.setFocusableInTouchMode(true);
                userPhone.setInputType(InputType.TYPE_CLASS_TEXT);
                userPhone.requestFocus();

                userDob.setCursorVisible(true);
                userDob.setFocusable(true);
                userDob.setEnabled(true);
                userDob.setClickable(true);
                userDob.setFocusableInTouchMode(true);
                userDob.setInputType(InputType.TYPE_CLASS_TEXT);
                userDob.requestFocus();
            }
        });
    }
}