package com.android.exsell.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.exsell.R;
import com.android.exsell.adapters.NotificationAdapter;
import com.android.exsell.cloudStorage.MyFirebaseStorage;
import com.android.exsell.db.AppSettingsDb;
import com.android.exsell.db.ItemDb;
import com.android.exsell.db.UserDb;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Category;
import com.android.exsell.models.Notifications;
import com.android.exsell.models.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NewListing extends AppCompatActivity implements FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch, FragmentTopBar.NotificationBellClickCallback, FragmentSearchBar.SearchBarBack {
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawerlist;
    RecyclerView notificationRecycler;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter adapter;
    NavigationView navigationView;
    Product addProduct;
    private Uri uri;
    private AppSettingsDb appSettingsDb;
    private String TAG = "NewListing ";
    private ItemDb itemDb;
    private MyFirebaseStorage myStorage;
    private ImageView search, wishlist, addListing, message, addImage, notification, hamburger, addListingIcon, profilePic;
    private Button addItem;
    private TextView title, description, tags, price, userName, userEmail;
    private LinearLayout progressLayout;
    private Spinner s;
    private AlertDialog.Builder builder;
    private static final int galleryPick = 1;
    private String productId, imageUrl;
    private Dialog dialog;
    private ImageView camera, gallery;
    private int flag = 0;
    String currentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_new_listing);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
        fragmentTransaction.commit();
        dialog = new Dialog(NewListing.this);
        dialog.setContentView(R.layout.layout_choose_image);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        itemDb = ItemDb.newInstance();
        myStorage = new MyFirebaseStorage();
        addProduct = new Product();
        appSettingsDb = AppSettingsDb.newInstance();

        layoutBottom = findViewById(R.id.layoutBottomBar);
        addListingIcon = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListingIcon.setImageResource(R.drawable.ic_additem);
        drawerlist = (DrawerLayout) findViewById(R.id.drawerLayoutItem);
        navigationView = findViewById(R.id.navigationMenuItem);
        navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));

        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) layoutBottom.findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));

        notificationRecycler = (RecyclerView) findViewById(R.id.right_drawer);
        notificationRecycler.setNestedScrollingEnabled(true);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerlist, R.string.nav_open, R.string.nav_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (flag == 1){

                    hamburger = (ImageView) findViewById(R.id.leftNavigationButton);
                    hamburger.setImageResource(R.drawable.ic_left_navigation_menu_button);
                    flag = 0;
                }
                else if(flag == 2){
                    notification = (ImageView) findViewById(R.id.notificationButton);
                    notification.setImageResource(R.drawable.ic_notifications);
                    flag = 0;
                }
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
                if(drawerlist.isDrawerOpen(GravityCompat.END)){
                    notification = (ImageView) findViewById(R.id.notificationButton);
                    notification.setImageResource(R.drawable.ic_notifications_black_24dp);
                    flag = 2;
                }
                else if(drawerlist.isDrawerOpen(GravityCompat.START)){
                    hamburger = (ImageView) findViewById(R.id.leftNavigationButton);
                    hamburger.setImageResource(R.drawable.ic_menu_open);
                    flag = 1;
                }

            }
        };
        drawerlist.addDrawerListener(mDrawerToggle);

        loadNotificationsRecycler(notificationRecycler, Notifications.getMyNotifications(), 1);

        s = (Spinner) findViewById(R.id.category);
        builder = new AlertDialog.Builder(this);

        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        tags = (TextView) findViewById(R.id.tags);
        price = (TextView) findViewById(R.id.price);
        addImage = (ImageView) findViewById(R.id.imageAdd);
        camera = dialog.findViewById(R.id.cameraClick);
        gallery = dialog.findViewById(R.id.galleryClick);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "Clicked camera");
                        if(ContextCompat.checkSelfPermission(NewListing.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(NewListing.this,
                                    new String[] {
                                            Manifest.permission.CAMERA
                                    }, 100);
                            openCamera();
                            dialog.dismiss();
                        } else{
                            openCamera();
                            dialog.dismiss();
                        }
                    }
                });
                gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "Clicked gallery");
                        openGallery();
                        dialog.dismiss();
                    }
                });
            }
        });

        addItem = (Button) findViewById(R.id.addListing);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem(new uploadCompleteCallback() {
                    @Override
                    public void onCallback(boolean uploaded) {
                        startActivity(new Intent(getApplicationContext(), MyListings.class));
                        finish();
                    }
                });
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getString("load") != null) {
            Log.i(TAG, " intent data "+extras);
            String title = extras.getString("title");
            String description =  extras.getString("description");
//            String tags = extras.getString("tags");
            String  imageUri = extras.getString("imageUri");
            String price = (extras.get("price").toString());
            this.title.setText(title);
            this.description.setText(description);
            this.price.setText(price);
            this.productId = extras.getString("productId");
            this.imageUrl = imageUri;
//            addListing.text
//            this.tags
            if(imageUri != null) {
                Picasso.get().load(imageUri).into(this.addImage);
            }
        }
        //dummy items in the category
        List<String> arraySpinner = Arrays.asList("Select an item", "Books", "Electronics", "Furniture", "Sports", "Stationery");
        appSettingsDb.getCategories(new AppSettingsDb.getCategoryCallback() {
            @Override
            public void onCallback(List<String> categories) {
                Log.i("TAG", "my Categories"+categories);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s.setAdapter(adapter);
            }
        });
    }

    public void loadNotificationsRecycler(RecyclerView thisRecycler, List<JSONObject> products, int columns) {
        layoutManager = new GridLayoutManager(this, columns);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager

        // create and set adapter
        adapter = new NotificationAdapter(products, this);
        thisRecycler.setAdapter(adapter);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 100);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == galleryPick) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    uri = imageUri;
                    Log.i(TAG, String.valueOf(uri));
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    addImage.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if(requestCode == 100) {
            if(resultCode == RESULT_OK) {
                Bitmap captureImage = (Bitmap) data.getExtras().get("data");
                addImage.setImageBitmap(captureImage);
                uri = getImageUri(getApplicationContext(), captureImage);
                Log.i(TAG, String.valueOf(uri));
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void addNewItem(uploadCompleteCallback callback) {
        setDialog(true);
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String itemTitle = title.getText().toString();
        String itemDescription = description.getText().toString();
        String sTags = tags.getText().toString();
        List<String> itemTags = new ArrayList<>();
        int itemPrice = Integer.parseInt(price.getText().toString());
        String itemSeller = currentFirebaseUser.getUid();

        addProduct.setTitle(itemTitle);
        addProduct.setCategories(Arrays.asList(s.getSelectedItem().toString()));
        addProduct.setDescription(itemDescription);
        addProduct.setSeller(itemSeller);
        if(this.imageUrl != null) {
            addProduct.setImageUri(this.imageUrl);
        }
        if(this.productId != null) {
            addProduct.setProductId(this.productId);
        }
        for(String tags: sTags.split(",")) {
            itemTags.add(tags.trim().toLowerCase());
        }
        addProduct.setTags(itemTags);
        addProduct.setPrice(itemPrice);
        itemDb.createItem(addProduct, productId, new ItemDb.createItemsCallback() {
            @Override
            public void onCallback(boolean ok, String id) {
                if(!ok) {
                    setDialog(false);
                    return;
                }
                Log.i(TAG,ok + " : id : "+id);
                if(uri == null) {
                    setDialog(false);
                    callback.onCallback(true);
                } else {
                    myStorage.uploadImage(uri, id, 0, new MyFirebaseStorage.downloadUrlCallback() {
                        @Override
                        public void onCallback(String url) {
                            Log.i(TAG," My URI "+url);
                            itemDb.getItemCollectionReference().document(id).update("imageUri", url);
                            setDialog(false);
                            callback.onCallback(true);
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerlist.closeDrawer(Gravity.LEFT, false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        title.setText("");
        description.setText("");
        tags.setText("");
        price.setText("");
    }
    public interface uploadCompleteCallback {
        void onCallback(boolean uploaded);
    }
    private void setDialog(boolean show) {
        Log.i(TAG, "Progress bar is up");
        builder.setView(R.layout.progressalert);
        builder.setCancelable(false);
        Dialog dialog = builder.create();
        if (show) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            dialog.show();
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            dialog.dismiss();
        }
    }

    @Override
    public void onHamburgerClickCallback() {
        Log.i(TAG,"onHamburgerClickCallback");
        if(drawerlist.isDrawerOpen(GravityCompat.START)){
            drawerlist.closeDrawer(GravityCompat.START);
        }
        else{
            drawerlist.closeDrawer(GravityCompat.END, false);
            drawerlist.openDrawer(GravityCompat.START);
            userName = (TextView) drawerlist.findViewById(R.id.userNameNav);
            userEmail = (TextView) drawerlist.findViewById(R.id.userEmailNav);
            profilePic = (ImageView) drawerlist.findViewById(R.id.profilePicNav);
            getUserDetails();

        }
    }

    @Override
    public void onNotificationBellClick() {
        if(drawerlist.isDrawerOpen(GravityCompat.END)) {
            drawerlist.closeDrawer(GravityCompat.END);
        }
        else {
            Log.i(TAG,"onNotificationBellClick");
            drawerlist.closeDrawer(GravityCompat.START, false);
            drawerlist.openDrawer(GravityCompat.END);
        }
    }

    public void setNavigationHeader() {

    }

    @Override
    public void onSearch(String search) {
        Log.i(TAG,"onSearch received "+search);
        //startActivity(new Intent(getApplicationContext(), Home.class));
    }

    @Override
    public void onSearchBack() {
        Log.i("onSearchBack", "searchBack");
    }
    public void getUserDetails(){
        userName.setText((String) UserDb.myUser.get("name"));
        userEmail.setText((String) UserDb.myUser.get("email"));
        if(UserDb.myUser.containsKey("imageUri")) {
            Picasso.get().load((String)UserDb.myUser.get("imageUri")).into(profilePic);
        }
    }

}