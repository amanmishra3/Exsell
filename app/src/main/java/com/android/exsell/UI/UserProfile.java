package com.android.exsell.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.exsell.R;
import com.android.exsell.db.UserDb;
import com.android.exsell.fragments.DatePickerFragment;
import com.android.exsell.models.Users;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity implements DatePickerFragment.OnDateSetListener{

    ImageView editProfile, addImage;
    Button updateProfile;
    TextView userName, userEmail, userPhone, userDob, userRating;
    LinearLayout linearEmail;
    private int galleryPick = 1;
    private CircleImageView myImage;
    private Uri uri;
    private UserDb userDb;
    private Users userObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        userDb = UserDb.newInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        editProfile = findViewById(R.id.editProfile);
        updateProfile = findViewById(R.id.profileUpdate);
        addImage = findViewById(R.id.addImage);
        myImage = (CircleImageView) findViewById(R.id.profilePic);

        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPhone = findViewById(R.id.userPhone);
        userDob = findViewById(R.id.userDOB);
        userRating = findViewById(R.id.userRating);
        if(userDb.myUser != null) {
            userName.setText((String) userDb.myUser.getOrDefault("fname", "My Name"));
            userEmail.setText((String) userDb.myUser.get("email"));
            userPhone.setText((String) userDb.myUser.get("contact"));
            if(userDb.myUser.containsKey("imageUri")) {
                Picasso.get().load((String)userDb.myUser.get("imageUri")).into(myImage);
            }
            if(userDb.myUser.containsKey("dob")) {
                Date d = (Date)(userDb.myUser.get("dob"));
                userDob.setText((d.getYear()+1900)+"/"+d.getMonth()+"/"+d.getDay());
            }
        } else {
            Log.i("Profile","myUser is null");
        }


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
                userPhone.setInputType(InputType.TYPE_CLASS_PHONE);
                userPhone.requestFocus();

                userDob.setCursorVisible(true);
                userDob.setFocusable(true);
                userDob.setEnabled(true);
                userDob.setClickable(true);
                userDob.setFocusableInTouchMode(true);
                userDob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog(view);
                    }
                });
                userDob.setInputType(InputType.TYPE_CLASS_TEXT);
                userDob.requestFocus();

                updateProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userDb.myUser.put("contact",userPhone.getText().toString());
                        userDb.myUser.put("fname",userName.getText().toString());

                        userDb.updateUserDetails(userDb.myUser, uri, new UserDb.updateUserCallback() {
                            @Override
                            public void onCallback(boolean updated) {
                                if(updated) {
                                    finish();
                                    startActivity(getIntent());
                                }
                                else{
                                    Log.i("UserProfile", "Massive Error updating user");
                                }
                            }
                        });
                    }
                });
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
//                addProduct.setImageUri(imageUri.toString());
                uri = imageUri;
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                myImage.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDate(Date date, int year, int month, int day) {
        Log.i("calendar","DOB: year "+year+"/"+month+"/"+day +date);
        userDob.setText(new String(year+"/"+month+"/"+day));
        userDb.myUser.put("dob", date);
    }
}